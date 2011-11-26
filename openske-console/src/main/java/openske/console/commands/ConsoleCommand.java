package openske.console.commands;

import java.util.List;

import jline.Completor;
import openske.console.Console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * Represents a console command which accepts parameters that are parsed using the Apache Commons CLI.
 *
 */
public abstract class ConsoleCommand implements Comparable<ConsoleCommand>, Completor {
    
    protected static final String OPTION_HELP = "help";
    
    protected String name;
    
    protected Options options;
    
    protected CommandLineParser commandLineParser;
    
    protected CommandLine commandLine;
    
    public ConsoleCommand(String cmdName) {
        name = cmdName;
        commandLineParser = new GnuParser();
        options = new Options();
        options.addOption(OptionBuilder.withDescription("Prints the command's help text.").create(OPTION_HELP));
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public int compareTo(ConsoleCommand cmd) {
        return name.compareToIgnoreCase(cmd.getName());
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public boolean parseArguments(String[] arguments) {
        try {
            commandLine = commandLineParser.parse(options, arguments);
            return true;
        } catch(ParseException e) {
            Console.println(e.getMessage());
            return false;
        }
    }
    
    public void printHelp() {
        Console.println("\n" + help() + "\n\n");
        new HelpFormatter().printHelp(name, options);
        Console.println("\n");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public int complete(String buffer, int cursor, List candidates) {
        if(Console.currentCommand.equalsIgnoreCase(name)) {
            for(Object opt : options.getOptions()) {
                candidates.add(((Option)opt).getOpt());
            }
        }
        return cursor;
    }
    
    public abstract void initialize();
    
    public abstract void execute();
    
    public abstract String help();

    public boolean isHelpNeeded() {
        return commandLine.hasOption(OPTION_HELP);
    }
}
