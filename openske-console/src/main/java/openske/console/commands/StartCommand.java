package openske.console.commands;

import java.io.File;

import openske.console.Console;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class StartCommand extends ConsoleCommand {

    protected final String OPTION_INFRASTRUCTURE_FILE = "infrastructureFile";
    protected final String OPTION_NESSUS_FILE = "nessusFile";

    public StartCommand() {
        super("start");
    }

    protected StartCommand(String name) {
        super(name);
    }

    @SuppressWarnings("static-access")
    @Override
    public void initialize() {
        Option infrastructure = OptionBuilder.withArgName("filepath").hasArg().withDescription("The infrastructure file to load").create(OPTION_INFRASTRUCTURE_FILE);

        Option nessusFile = OptionBuilder.withArgName("filepath").hasArg().withDescription("The Nessus V2 file to load").create(OPTION_NESSUS_FILE);

        options.addOption(infrastructure).addOption(nessusFile);
    }

    @Override
    public void execute() {
        if(Console.currentCommandArgs.length == 0) {
            printHelp();
            return;
        }
        if (commandLine.hasOption(OPTION_INFRASTRUCTURE_FILE)) {
            Console.engine.setInfrastructureFile(new File(commandLine.getOptionValue(OPTION_INFRASTRUCTURE_FILE)));
        }
        if (commandLine.hasOption(OPTION_NESSUS_FILE)) {
            Console.engine.setNessusFile(new File(commandLine.getOptionValue(OPTION_NESSUS_FILE)));
        }
        Console.engine.start();
    }

    @Override
    public String help() {
        return "Starts the OpenSKE engine.";
    }

}
