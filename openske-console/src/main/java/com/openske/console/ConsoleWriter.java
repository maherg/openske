package com.openske.console;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ConsoleWriter extends PrintWriter {

    public ConsoleWriter(PrintStream printStream) {
        super(printStream, true);
    }
    
    @Override
    public PrintWriter format(String format, Object... args ) {
        super.format(format + "\n", args);
        return this;
    }
}
