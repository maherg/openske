package com.openske.drools;

import java.io.PrintWriter;
import java.util.Collection;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class DroolsFacade {

    protected KnowledgeBuilder knowledgeBuilder;
    protected KnowledgeBase knowledgeBase;
    protected PrintWriter outputWriter;
    protected StatefulKnowledgeSession session;
    protected KnowledgeRuntimeLogger logger;

    public DroolsFacade() {
        knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    }
    
    public void loadRules() throws Throwable {
        String[] drlFiles = new String[] { "com/openske/assets/Assets.drl", "com/openske/policy/Policy.drl", "com/openske/software/Software.drl", "com/openske/Validation.drl" };
        outputWriter.format("Loading %d rules from the classpath...", drlFiles.length);
        for(String drl : drlFiles) {
            outputWriter.format("\t - Loading '%s' ...", drl);
            knowledgeBuilder.add(ResourceFactory.newClassPathResource(drl), ResourceType.DRL);
            if(knowledgeBuilder.hasErrors()) {
                outputWriter.format(knowledgeBuilder.getErrors().toString());
                throw new RuntimeException("Found errors in DRL file : " + drl);
            }
        }
        
        knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        Collection<KnowledgePackage> knowledgePackages = knowledgeBuilder.getKnowledgePackages();
        outputWriter.format("Adding %d compiled knowledge packages to the knowledgebase...", knowledgePackages.size());
        for(KnowledgePackage kpackage : knowledgePackages) {
            outputWriter.format("\t - Knowledge Package %s (%d rules)", kpackage.getName(), kpackage.getRules().size());
        }
        knowledgeBase.addKnowledgePackages(knowledgePackages);
    }
    
    public void loadFacts() {
        getSession();
    }

    public void fireAllRules() {
        
    }
    
    public void cleanup() {
        closeSession();
    }
    
    protected StatefulKnowledgeSession getSession() {
        if(session == null) {
            session = knowledgeBase.newStatefulKnowledgeSession();
            outputWriter.format("Opened new Drools session (%d)", session.getId());
            logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);
        }
        return session;
    }
    
    protected void closeSession() {
        if(session != null) {
            logger.close();
            int sessionId = session.getId();
            session.dispose();
            outputWriter.format("Closed Drools session (%d)", sessionId);
            session = null;
        }
    }

    public PrintWriter getOutputWriter() {
        return outputWriter;
    }

    public void setOutputWriter(PrintWriter outputWriter) {
        this.outputWriter = outputWriter;
    }
}
