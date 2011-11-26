package openske.console.commands;

import openske.console.Console;

public class RestartCommand extends StartCommand {
    
    public RestartCommand() {
        super("restart");
    }

    @Override
    public void execute() {
        Console.getCommand("stop").execute();
        super.execute();
    }

    @Override
    public String help() {
        return "Restarts the OpenSKE engine.";
    }

}
