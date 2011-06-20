package com.openske.console.commands;

import com.openske.console.Console;
import com.openske.engine.Engine;

public class ExitCommand extends ConsoleCommand {
    
    public ExitCommand() {
        super("exit");
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        Engine.getInstance().stop();
        Console.exitNow = true;
    }

    @Override
    public String help() {
        return "Exits the console.";
    }

}
