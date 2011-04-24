package com.openske.console;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.SimpleCompletor;

import com.openske.engine.Engine;
import com.openske.engine.EngineMode;

public class Console {

    public Console() {

    }

    public static void main(String[] args) throws Throwable {
        ConsoleWriter consoleWriter = new ConsoleWriter(System.out);
        ConsoleReader consoleReader = new ConsoleReader(System.in,
                consoleWriter);
        // consoleReader.setDefaultPrompt("\033[1;36mopenske>\033[m ");
        consoleReader.setDefaultPrompt("openske> ");

        consoleReader.addCompletor(new ArgumentCompletor(new SimpleCompletor(
                ConsoleCommand.valueNames())));

        // MAIN LOOP
        // consoleWriter.print("\033[1;33m");
        consoleWriter.printf("Welcome to OpenSKE (JVM: %s) !", System
                .getProperty("java.version"));
        consoleWriter.printf("Type 'help' for help\n");
        // consoleWriter.print("\033[m");
        String line = null;
        Engine engine = new Engine();
        engine.setOutputWriter(consoleWriter);
        try {
            while ((line = consoleReader.readLine()) != null) {
                line = line.trim();
                if (line.equals("")) {
                    continue;
                }
                boolean exitConsole = false;
                try {
                    // Parse the command line input.
                    String[] lineParts = line.split(" ");
                    String command = lineParts[0];
                    String[] commandArgs = new String[lineParts.length - 1];
                    System.arraycopy(lineParts, 1, commandArgs, 0, lineParts.length - 1);
                    // Delegate the command execution upon the command name.
                    if (ConsoleCommand.exists(command)) {
                        ConsoleCommand cmd = ConsoleCommand.valueOf(command.toUpperCase());
                        switch (cmd) {
                        case EXIT:
                            exitConsole = true;
                            break;
                        case START:
                            engine.start(commandArgs);
                            break;
                        case STOP:
                            engine.stop();
                            break;
                        case RESTART:
                            engine.stop();
                            engine.start(commandArgs);
                            break;
                        case HELP:
                            consoleWriter.print(ConsoleCommand.displayHelp());
                            break;
                        case CLEAR:
                            consoleReader.clearScreen();
                            break;
                       case BENCHMARK:
                           engine.setMode(EngineMode.BENCHMARK);
			   consoleWriter.setQuiet(true);
                           engine.start(commandArgs);
			   engine.stop();
                           break;
                        default:
                            consoleWriter.println("This command is not implemented yet !");
                        }
                    } else {
                        consoleWriter.printf("Unknown command : %s", command);
                        continue;
                    }

                } catch (Exception e) {
                    consoleWriter.printf("Failed to execute : '%s'", line);
                    e.printStackTrace(consoleWriter);
                    continue;
                }
                if (exitConsole) {
                    break;
                }
            }
        } finally {
            // Write a new line if CTRL-D was pressed
            if (line == null) {
                consoleWriter.println();
            }
            // Upon exiting the console, stop the engine if we started it
            if (engine.isStarted()) {
                engine.stop();
            }
        }
    }
}
