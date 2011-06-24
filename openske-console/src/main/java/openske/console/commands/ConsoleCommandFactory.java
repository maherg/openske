package openske.console.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import openske.console.ConsoleException;


public class ConsoleCommandFactory {

    private static Map<String, ConsoleCommand> commands = new HashMap<String, ConsoleCommand>();
    
    public static ConsoleCommand create(Class<?> klass) throws Exception {
        ConsoleCommand cmd = (ConsoleCommand) klass.getConstructor(new Class[] {}).newInstance(new Object[] {});
        if (commands.containsKey(cmd.getName().toLowerCase())) {
            throw new ConsoleException("Attempted to create a ConsoleCommand that already existed before : " + cmd.getName());
        } else {
            cmd.initialize();
            commands.put(cmd.getName().toLowerCase(), cmd);
            return cmd;
        }
    }

    public static Collection<ConsoleCommand> listCommands() {
        return commands.values();
    }
    
    public static Map<String, ConsoleCommand> getCommands() {
        return commands;
    }
    
    public static String[] listCommandNames() {
        return commands.keySet().toArray(new String[0]);
    }

    public static boolean hasCommand(String name) {
        return name != null && commands.containsKey(name.toLowerCase());
    }

    public static ConsoleCommand getCommand(String name) {
        return hasCommand(name) ? commands.get(name.toLowerCase()) : null;
    }
}
