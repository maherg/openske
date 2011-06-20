package com.openske.console.commands;

import java.io.File;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import com.openske.console.Console;

public class StartCommand extends ConsoleCommand {

    public final String OPTION_INFRASTRUCTURE_FILE = "infrastructureFile";
    public final String OPTION_NESSUS_FILE = "nessusFile";

    public StartCommand() {
        super("start");
    }
    
    protected StartCommand(String name) {
        super(name);
    }

    @SuppressWarnings("static-access")
    @Override
    public void initialize() {
        Option infrastructure = OptionBuilder.withArgName("filepath").hasArg().isRequired().withDescription("The infrastructure file to load").create(OPTION_INFRASTRUCTURE_FILE);

        Option nessusFile = OptionBuilder.withArgName("filepath").hasArg().isRequired(false).withDescription("The Nessus V2 file to load").create(OPTION_NESSUS_FILE);

        options.addOption(infrastructure).addOption(nessusFile);
    }

    @Override
    public void execute() {
        Console.engine.setInfrastructureFile(new File(commandLine.getOptionValue(OPTION_INFRASTRUCTURE_FILE)));
        if(commandLine.hasOption(OPTION_NESSUS_FILE)) {
            Console.engine.setNessusFile(new File(commandLine.getOptionValue(OPTION_NESSUS_FILE)));
        }
        Console.engine.start();
    }

    @Override
    public String help() {
        return "Starts the OpenSKE engine.";
    }

}
