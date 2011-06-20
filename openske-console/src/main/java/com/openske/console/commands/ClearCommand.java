package com.openske.console.commands;

import com.openske.console.Console;

public class ClearCommand extends ConsoleCommand {
    
    public ClearCommand() {
        super("clear");
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        Console.clearScreen();
    }

    @Override
    public String help() {
        return "Clears the console screen.";
    }

}
