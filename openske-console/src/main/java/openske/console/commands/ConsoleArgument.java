package openske.console.commands;

public class ConsoleArgument {
    
    protected String name;
    protected String value;
    protected boolean required;
    
    public ConsoleArgument(String argName, boolean required) {
        this.name = argName;
        this.value = "";
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

}
