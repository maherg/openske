package openske.console;

public class ConsoleException extends Exception {

    private static final long serialVersionUID = 1L;

    public ConsoleException(String msg) {
        super(msg);
    }
    
    public ConsoleException(String msg, Throwable t) {
        super(msg, t);
    }
}
