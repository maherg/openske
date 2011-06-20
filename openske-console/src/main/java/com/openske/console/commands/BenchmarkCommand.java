package com.openske.console.commands;

import com.openske.console.Console;
import com.openske.engine.EngineMode;

public class BenchmarkCommand extends StartCommand {

    public BenchmarkCommand() {
        super("benchmark");
    }

    @Override
    public void execute() {
        Console.engine.setMode(EngineMode.BENCHMARK);
        Console.writer.setQuiet(true);
        super.execute();
        Console.engine.stop();
    }

    @Override
    public String help() {
        return "Benchmarks the OpenSKE engine.";
    }

}
