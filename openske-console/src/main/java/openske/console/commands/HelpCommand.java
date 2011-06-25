package openske.console.commands;

import openske.console.Console;

public class HelpCommand extends ConsoleCommand {
    
    public HelpCommand() {
        super("help");
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        if(Console.currentCommandArgs.length > 0) {
            for(String cmdName : Console.currentCommandArgs) {
                if(Console.hasCommand(cmdName)) {
                    ConsoleCommand cmd = Console.getCommand(cmdName);
                    Console.println("\n" + cmd.help() + "\n\n");
                    cmd.printOptionsHelp();
                    Console.println("\n");
                }
            }
        } else {
            Console.println("\nThe following are the available console commands.\n");
            for(ConsoleCommand cmd : Console.listCommands()) {
                Console.println(helpEntry(cmd));
            }
            Console.println("\n");
        }
    }
    
    private String helpEntry(ConsoleCommand cmd) {
        return String.format("%-20s %s", cmd.toString(), cmd.help());
    }

    @Override
    public String help() {
        return "Displays the console help.";
    }

}
