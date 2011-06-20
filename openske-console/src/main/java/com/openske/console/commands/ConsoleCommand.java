package com.openske.console.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.openske.console.Console;

/**
 * Represents a console command which accepts parameters that are parsed using the Apache Commons CLI.
 *
 */
public abstract class ConsoleCommand implements Comparable<ConsoleCommand> {
    
    protected String name;
    
    protected Options options;
    
    protected CommandLineParser commandLineParser;
    
    protected CommandLine commandLine;
    
    public ConsoleCommand(String name) {
        this.name = name;
        this.commandLineParser = new GnuParser();
        this.options = new Options();
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
    
    public void printOptionsHelp() {
        new HelpFormatter().printHelp(name, options);
    }
    
    public abstract void initialize();
    
    public abstract void execute();
    
    public abstract String help();
}
