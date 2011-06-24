package openske.console.commands;

import java.util.List;

import jline.Completor;


public class ConsoleCommandCompletor implements Completor {
    
    @SuppressWarnings("rawtypes")
    @Override
    public int complete(String buffer, int cursor, List candidates) {
        String commandName = buffer.split(" ")[0];
        if(ConsoleCommandFactory.getCommands().containsKey(commandName)) {
            return ConsoleCommandFactory.getCommands().get(commandName).complete(buffer, cursor, candidates);
        } else {
            return cursor;
        }
    }
}
