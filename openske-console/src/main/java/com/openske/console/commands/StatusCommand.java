package com.openske.console.commands;

import com.openske.console.Console;
import com.openske.engine.Engine;

public class StatusCommand extends ConsoleCommand {

    public StatusCommand() {
        super("status");
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        Console.println("\n" + Engine.getInstance().status() + "\n");
    }

    @Override
    public String help() {
        return "Prints the current status of the OpenSKE engine.";
    }

}
