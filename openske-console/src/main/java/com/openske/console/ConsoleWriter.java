package com.openske.console;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ConsoleWriter extends PrintWriter {
    
    protected String lineSeparator;

    public ConsoleWriter(PrintStream printStream) {
        super(printStream, true);
        lineSeparator = System.getProperty("line.separator");
    }
    
    @Override
    public PrintWriter format(String format, Object... args ) {
        if(!format.endsWith(lineSeparator)) {
            format = format + lineSeparator;
        }
        super.format(format, args);
        return this;
    }
}
