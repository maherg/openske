package com.openske.drools;

import java.io.PrintWriter;

import org.drools.event.rule.ActivationCancelledEvent;
import org.drools.event.rule.ActivationCreatedEvent;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.AgendaGroupPoppedEvent;
import org.drools.event.rule.AgendaGroupPushedEvent;
import org.drools.event.rule.BeforeActivationFiredEvent;

public class DroolsAgendaEventListener implements AgendaEventListener {
    
    protected PrintWriter outputWriter;

    @Override
    public void activationCancelled(ActivationCancelledEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void activationCreated(ActivationCreatedEvent event) {
        outputWriter.format("[DROOLS] Activation Created : %s", event.getActivation().getRule().getName());
    }

    @Override
    public void beforeActivationFired(BeforeActivationFiredEvent event) {
        outputWriter.format("[DROOLS] Activation Fired : %s", event.getActivation().getRule().getName());

    }

    @Override
    public void afterActivationFired(AfterActivationFiredEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void agendaGroupPushed(AgendaGroupPushedEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
        // TODO Auto-generated method stub

    }

    public PrintWriter getOutputWriter() {
        return outputWriter;
    }

    public void setOutputWriter(PrintWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

}
