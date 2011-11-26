package openske.drools;

import java.io.PrintWriter;

import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;

public class DroolsProcessEventListener implements ProcessEventListener {
    
    protected PrintWriter outputWriter;

    @Override
    public void afterNodeLeft(ProcessNodeLeftEvent event) {
        // TODO Auto-generated method stub
        
        
    }

    @Override
    public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
        // TODO Auto-generated method stub
        
        
    }

    @Override
    public void afterProcessCompleted(ProcessCompletedEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterProcessStarted(ProcessStartedEvent event) {
        // TODO Auto-generated method stub
        
        
    }

    @Override
    public void beforeNodeLeft(ProcessNodeLeftEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
        outputWriter.printf("Entering node '%s'...", event.getNodeInstance().getNodeName());
        
    }

    @Override
    public void beforeProcessCompleted(ProcessCompletedEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeProcessStarted(ProcessStartedEvent event) {
        outputWriter.printf("Starting process '%s'...", event.getProcessInstance().getProcessId());
        
    }

    public PrintWriter getOutputWriter() {
        return outputWriter;
    }

    public void setOutputWriter(PrintWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

}
