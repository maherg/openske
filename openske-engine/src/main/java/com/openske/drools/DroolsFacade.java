package com.openske.drools;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
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
        
    }
    
    @SuppressWarnings("unchecked")
    public void loadRules() throws Throwable {
        
        // Search for DRL files starting from the current directory
        File rootDir = new File(".");
        Collection<File> drlFiles = FileUtils.listFiles(rootDir, new String[] { "drl" }, true);
        outputWriter.format("Loading rules from %s...", rootDir.getAbsolutePath());
        
        // Load the found DRL files into a new KnowledgeBuilder
        knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        for(File drl : drlFiles) {
            // Skip /target/ entries
            if(drl.getAbsolutePath().contains("/target/")) {
                continue;
            }
            outputWriter.format("\t - Loading '%s' ...", drl.getPath());
            knowledgeBuilder.add(ResourceFactory.newFileResource(drl.getAbsolutePath()), ResourceType.DRL);
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
        outputWriter.format("Firing all rules...");
        int rulesFired = getSession().fireAllRules();
        outputWriter.format("Fired %d rules", rulesFired);
    }
    
    public void cleanup() {
        closeSession();
    }
    
    protected StatefulKnowledgeSession getSession() {
        if(session == null) {
            session = knowledgeBase.newStatefulKnowledgeSession();
            outputWriter.format("Opened new Drools session (%s)", session.toString());
            logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);
        }
        return session;
    }
    
    protected void closeSession() {
        if(session != null) {
            logger.close();
            String sessionStr = session.toString();
            session.dispose();
            outputWriter.format("Closed Drools session (%s)", sessionStr);
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
