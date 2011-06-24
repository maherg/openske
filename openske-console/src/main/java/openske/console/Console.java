package openske.console;

import java.io.File;
import java.io.IOException;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.History;
import jline.SimpleCompletor;
import openske.console.commands.BenchmarkCommand;
import openske.console.commands.ClearCommand;
import openske.console.commands.ConsoleCommand;
import openske.console.commands.ConsoleCommandFactory;
import openske.console.commands.ExitCommand;
import openske.console.commands.HelpCommand;
import openske.console.commands.RestartCommand;
import openske.console.commands.StartCommand;
import openske.console.commands.StatusCommand;
import openske.console.commands.StopCommand;
import openske.engine.Engine;


public class Console {
    
    public static final File historyFile = new File(System.getProperty("user.home") + File.separator + ".openske_history"); 

    public static ConsoleWriter writer;
    public static ConsoleReader reader;
    public static History history;
    public static String currentLine;
    public static String currentCommand;
    public static String[] currentCommandArgs;
    public static Engine engine;
    public static boolean exitNow = false;

    private static void initialize() throws Exception {
        writer = new ConsoleWriter(System.out);
        reader = new ConsoleReader(System.in, writer);
        historyFile.createNewFile();
        history = new History(historyFile);
        reader.setHistory(history);
        // consoleReader.setDefaultPrompt("\033[1;36mopenske>\033[m ");
        reader.setDefaultPrompt("OpenSKE > ");
        ConsoleCommandFactory.create(BenchmarkCommand.class);
        ConsoleCommandFactory.create(ClearCommand.class);
        ConsoleCommandFactory.create(ExitCommand.class);
        ConsoleCommandFactory.create(HelpCommand.class);
        ConsoleCommandFactory.create(RestartCommand.class);
        ConsoleCommandFactory.create(StartCommand.class);
        ConsoleCommandFactory.create(StopCommand.class);
        ConsoleCommandFactory.create(StatusCommand.class);
        reader.addCompletor(new ArgumentCompletor(new SimpleCompletor(ConsoleCommandFactory.listCommandNames())));
        currentLine = null;

        engine = Engine.getInstance();
        engine.setOutputWriter(writer);
    }

    private static void welcomeMessage() {
        writer.printf("Welcome to OpenSKE (JVM: %s) !", System.getProperty("java.version"));
        writer.printf("Type 'help' for help\n");
    }

    private static void mainLoop() throws Exception {
        try {
            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine.trim();
                if (currentLine.equals("")) {
                    continue;
                }
                history.addToHistory(currentLine);
                boolean exitConsole = false;
                try {
                    // Parse the command line input.
                    String[] lineParts = currentLine.split(" ");
                    currentCommand = lineParts[0];
                    currentCommandArgs = new String[lineParts.length - 1];
                    System.arraycopy(lineParts, 1, currentCommandArgs, 0, lineParts.length - 1);
                    if (ConsoleCommandFactory.hasCommand(currentCommand)) {
                        ConsoleCommand cmd = ConsoleCommandFactory.getCommand(currentCommand);
                        if (cmd.parseArguments(currentCommandArgs)) {
                            cmd.execute();
                        }
                    } else {
                        writer.printf("Unknown command : %s", currentCommand);
                        continue;
                    }
                } catch (Exception e) {
                    writer.printf("Failed to execute : '%s'", currentLine);
                    e.printStackTrace(writer);
                    continue;
                }
                if (exitConsole) {
                    break;
                }
            }
        } finally {

        }
    }

    private static void cleanup() throws IOException {
        // Write a new line if CTRL-D was pressed
        if (currentLine == null) {
            writer.println();
        }
        // Upon exiting the console, stop the engine if we started it
        if (engine.isStarted()) {
            engine.stop();
        }
    }

    public static void println(String text, Object... args) {
        writer.printf(text, args);
    }

    public static void clearScreen() {
        try {
            reader.clearScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        initialize();

        welcomeMessage();

        mainLoop();

        cleanup();
    }
}
