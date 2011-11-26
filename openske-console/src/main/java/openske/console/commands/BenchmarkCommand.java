package openske.console.commands;

import openske.console.Console;
import openske.engine.EngineMode;


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
