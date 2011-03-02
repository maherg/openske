package com.openske.console;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ConsoleWriter extends PrintWriter {
    
    protected String lineSeparator = System.getProperty("line.separator");
    protected boolean quiet = false;

    public ConsoleWriter(PrintStream printStream) {
        super(printStream, true);
    }
    
    @Override
    public PrintWriter printf(String format, Object... args ) {
	if(this.quiet && !format.startsWith("[OPENSKE]")) {
	    return this;
	}
        if(!format.endsWith(lineSeparator)) {
            format = format + lineSeparator;
        }
        super.printf(format, args);
        return this;
    }

    public void setQuiet(boolean quiet) {
	this.quiet = quiet;
    }

    public boolean isQuiet() {
	return this.quiet;
    }
}
