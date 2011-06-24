package openske.console.commands;

public class RestartCommand extends StartCommand {
    
    public RestartCommand() {
        super("restart");
    }

    @Override
    public void execute() {
        ConsoleCommandFactory.getCommand("stop").execute();
        super.execute();
    }

    @Override
    public String help() {
        return "Restarts the OpenSKE engine.";
    }

}
