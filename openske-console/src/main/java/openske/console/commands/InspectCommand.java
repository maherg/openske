package openske.console.commands;

import openske.console.Console;
import openske.engine.Engine;
import openske.model.Infrastructure;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class InspectCommand extends ConsoleCommand {
    
    public static final String OPTION_ITEM_CLASS = "itemClass";
    public static final String OPTION_ITEM_NAME = "itemName";

    public InspectCommand() {
        super("inspect");
    }

    @SuppressWarnings("static-access")
    @Override
    public void initialize() {
        Option typeClass = OptionBuilder.hasArg().withDescription("The class of the item to be inspected.").withArgName("className").create(OPTION_ITEM_CLASS);
        Option typeName  = OptionBuilder.hasArg().withDescription("The name of the item to be inspected.").withArgName("name").create(OPTION_ITEM_NAME);
        options.addOption(typeClass).addOption(typeName);
    }

    
    @Override
    public void execute() {
        if(!commandLine.hasOption(OPTION_ITEM_CLASS)) {
            Console.println("Missing argument " , OPTION_ITEM_CLASS);
            return;
        }
        if(!commandLine.hasOption(OPTION_ITEM_NAME)) {
            Console.println("Missing argument " , OPTION_ITEM_NAME);
            return;
        }
        
        try {
            Class klass = Class.forName(commandLine.getOptionValue(OPTION_ITEM_CLASS));
            Infrastructure infra = Engine.getInstance().getInfrastructure();
            String itemName = commandLine.getOptionValue(OPTION_ITEM_NAME);
            if(infra.has(klass, itemName)) {
                Console.println(infra.get(klass, itemName).inspect());
            } else {
                Console.println("Item doesn't exist.");
            }
            
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        return "Inspect items in the infrastructure.";
    }

}
