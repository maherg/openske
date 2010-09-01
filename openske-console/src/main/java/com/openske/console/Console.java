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
        ConsoleReader consoleReader = new ConsoleReader(System.in, consoleWriter);
        consoleReader.setDefaultPrompt("openske> ");

        consoleReader.addCompletor(new ArgumentCompletor(new SimpleCompletor(
                ConsoleCommand.valueNames())));

        // MAIN LOOP
        consoleWriter.format("Welcome to OpenSKE (JVM: %s) !", System
                .getProperty("java.version"));
        consoleWriter.format("Type 'help' for help\n");
        String line;
        Engine engine = new Engine();
        engine.setOutputWriter(consoleWriter);
        while ((line = consoleReader.readLine()) != null) {
            line = line.trim();
            if (line.equals("")) {
                continue;
            }
            boolean exitConsole = false;
            try {
                ConsoleCommand cmd = ConsoleCommand.valueOf(line.toUpperCase());
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
                    if(engine.isStarted()) {
                        engine.stop();
                    }
                    break;
                case RESTART:
                    engine.stop();
                    engine.run();
                    break;
                case HELP:
                    consoleWriter.println("The following are the available commands :");
                    consoleWriter.print(ConsoleCommand.displayHelp());
                    break;
                case BEANSHELL:
                    break;
                }
            } catch (Exception e) {
                consoleWriter.format("Failed to execute : '%s'", line);
                e.printStackTrace(consoleWriter);
            }
            if (exitConsole) {
                break;
            }
        }
        // Write a new line if CTRL-D was pressed
        if(line == null) {
            consoleWriter.println();
        }
        // Upon exiting the console, stop the engine if we started it
        if(engine.isStarted()) {
            engine.stop();
        }
    }
}
