package com.openske.console;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.SimpleCompletor;

public class Console {

    public static void main(String[] args) throws Exception {
        ConsoleReader reader = new ConsoleReader();

        reader.addCompletor(new ArgumentCompletor(new SimpleCompletor(
                ConsoleCommand.valueNames())));

        // MAIN LOOP
        System.out.println("OpenSKE Console !");
        String line;
        while ((line = reader.readLine("openske> ")) != null) {
            boolean exitConsole = false;
            try {
                ConsoleCommand cmd = ConsoleCommand.valueOf(line.trim().toUpperCase());
                switch(cmd) {
                case EXIT:
                case QUIT:
                    exitConsole = true;
                    break;
                case HELP:
                    break;
                case BEANSHELL:
                    break;
                }
            } catch(Exception e) {
                System.out.println("Unknown command : '" + line + "'");
            }
            if(exitConsole) {
                break;
            }
        }
    }
}
