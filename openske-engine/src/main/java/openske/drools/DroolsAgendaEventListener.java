package openske.drools;

import java.io.PrintWriter;

import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;

public class DroolsAgendaEventListener implements AgendaEventListener {
    
    protected PrintWriter outputWriter;

    @Override
    public void matchCreated(MatchCreatedEvent event) {
        outputWriter.printf("[DROOLS] Activation Created : %s", event.getMatch().getRule().getName());
    }

    @Override
    public void matchCancelled(MatchCancelledEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent event) {
        outputWriter.printf("[DROOLS] Activation Fired : %s", event.getMatch().getRule().getName());
    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
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

    @Override
    public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
        // TODO Auto-generated method stub
    }

    public PrintWriter getOutputWriter() {
        return outputWriter;
    }

    public void setOutputWriter(PrintWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

}
