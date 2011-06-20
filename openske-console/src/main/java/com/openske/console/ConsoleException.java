package com.openske.console;

public class ConsoleException extends Exception {

    public ConsoleException(String msg) {
        super(msg);
    }
    
    public ConsoleException(String msg, Throwable t) {
        super(msg, t);
    }
}
