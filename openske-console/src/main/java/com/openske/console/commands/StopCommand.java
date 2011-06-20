package com.openske.console.commands;

import com.openske.engine.Engine;

public class StopCommand extends ConsoleCommand {
    
    public StopCommand() {
        super("stop");
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        Engine.getInstance().stop();
    }

    @Override
    public String help() {
        return "Stops the OpenSKE engine.";
    }

}
