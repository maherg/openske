package com.openske.console;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.SimpleCompletor;

import com.openske.engine.Engine;

public class Console {

    public Console() {

    }

    public static void main(String[] args) throws Throwable {
        ConsoleWriter consoleWriter = new ConsoleWriter(System.out);
        ConsoleReader consoleReader = new ConsoleReader(System.in,
                consoleWriter);
        consoleReader.setDefaultPrompt("\033[1;36mopenske>\033[m ");

        consoleReader.addCompletor(new ArgumentCompletor(new SimpleCompletor(
                ConsoleCommand.valueNames())));

        // MAIN LOOP
        consoleWriter.print("\033[1;33m");
        consoleWriter.format("Welcome to OpenSKE (JVM: %s) !", System
                .getProperty("java.version"));
        consoleWriter.format("Type 'help' for help\n");
        consoleWriter.print("\033[m");
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
                    if (ConsoleCommand.exists(line)) {
                        ConsoleCommand cmd = ConsoleCommand.valueOf(line
                                .toUpperCase());
                        switch (cmd) {
                        case EXIT:
                        case QUIT:
                            exitConsole = true;
                            break;
                        case START:
                            if (!engine.isStarted()) {
                                engine.run();
                            }
                            break;
                        case STOP:
                            if (engine.isStarted()) {
                                engine.stop();
                            }
                            break;
                        case RESTART:
                            engine.stop();
                            engine.run();
                            break;
                        case HELP:
                            consoleWriter
                                    .println("The following commands are available :");
                            consoleWriter.print(ConsoleCommand.displayHelp());
                            break;
                        case BEANSHELL:
                            break;
                        }
                    } else {
                        consoleWriter.format("Unknown command : %s", line);
                        continue;
                    }

                } catch (Exception e) {
                    consoleWriter.format("Failed to execute : '%s'", line);
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
