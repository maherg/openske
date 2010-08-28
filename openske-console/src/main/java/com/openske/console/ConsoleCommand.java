package com.openske.console;

import java.util.ArrayList;
import java.util.List;

public enum ConsoleCommand {
    HELP,
    BEANSHELL,
    EXIT,
    QUIT,
    ;
    
    public String toString() {
        return this.name().toLowerCase();
    }
    
    public static String[] valueNames() {
        List<String> cmdList = new ArrayList<String>();
        for(ConsoleCommand cmd : ConsoleCommand.values()) {
            cmdList.add(cmd.toString());
        }
        return cmdList.toArray(new String[0]);
    }
    
}
