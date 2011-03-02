package com.openske.drools;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.openske.engine.Engine;
import com.openske.engine.EngineMode;
import org.codehaus.groovy.control.CompilationFailedException;
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
    
    protected KnowledgeBase knowledgeBase;
    protected PrintWriter outputWriter;
    protected StatefulKnowledgeSession session;
    protected KnowledgeRuntimeLogger droolsLogger;
    protected boolean initialized;

    public void initialize() {
        if (!isInitialized()) {
            outputWriter.printf("[DROOLS] Initializing Drools Knowledge Base...");
            knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
            initialized = true;
        }
    }

    public void loadRules() throws Throwable {
        if (isInitialized()) {
            // Search for DRL files starting from the current directory
            Collection<File> drlFiles = DroolsResourceHelper.listResources(ResourceType.DRL.getDefaultExtension());
            outputWriter.printf("[DROOLS] Loading %d rule files...", drlFiles.size());
            // Load the found DRL files into the KnowledgeBuilder
            KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            for (File drl : drlFiles) {
                outputWriter.printf("\t - Loading '%s' ...", drl.getPath());
                knowledgeBuilder.add(ResourceFactory.newFileResource(drl.getAbsolutePath()), ResourceType.DRL);
                if (knowledgeBuilder.hasErrors()) {
                    outputWriter.printf(knowledgeBuilder.getErrors().toString());
                    throw new RuntimeException("Found errors in DRL file : " + drl);
                }
            }

            Collection<KnowledgePackage> knowledgePackages = knowledgeBuilder.getKnowledgePackages();
            outputWriter.printf("[DROOLS] Adding %d compiled knowledge packages to the knowledgebase...", knowledgePackages.size());
            for (KnowledgePackage kpackage : knowledgePackages) {
                outputWriter.printf("\t - Knowledge Package %s (%d rules)", kpackage.getName(), kpackage.getRules().size());
            }
            knowledgeBase.addKnowledgePackages(knowledgePackages);
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void loadFacts(String[] filenames) {
        if (isInitialized()) {
            if(filenames == null || filenames.length == 0) {
                outputWriter.printf("[DROOLS] No fact files have been provided to be loaded !");
            } else {
                try {
                    // Prepare the binding
                    Binding params = new Binding();
                    params.setVariable("session", getSession());
                    // Launch the Groovy shell for the files matched
                    GroovyShell shell = new GroovyShell(params);
                    Collection<File> factFiles = DroolsResourceHelper.listResources("groovy");
                    List<String> filenamesList = Arrays.asList(filenames);
                    for(File factFile : factFiles) {
                        if(filenamesList.contains(factFile.getName())) {
                            outputWriter.printf("[DROOLS] Loading fact file : %s", factFile.getPath());
                            shell.evaluate(factFile);
                        }
                    }
                } catch (CompilationFailedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void loadProcesses() {
        if (isInitialized()) {
            Collection<File> rfFiles = DroolsResourceHelper.listResources(ResourceType.DRF.getDefaultExtension());
            outputWriter.printf("[DROOLS] Loading %d processes...", rfFiles.size());
            KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            for (File rf : rfFiles) {
                knowledgeBuilder.add(ResourceFactory.newFileResource(rf), ResourceType.DRF);
                if(knowledgeBuilder.hasErrors()) {
                    outputWriter.printf(knowledgeBuilder.getErrors().toString());
                    throw new RuntimeException("[DROOLS] Found errors in DRF file : " + rf);
                }
            }
            knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void fireRules() {
        if (isInitialized()) {
            outputWriter.printf("[DROOLS] Firing all rules...");
            getSession().fireAllRules();
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void startProcesses() {
        if (isInitialized()) {
            Collection<File> rfFiles = DroolsResourceHelper.listResources(ResourceType.DRF.getDefaultExtension());
            outputWriter.printf("[DROOLS] Starting %d processes...", rfFiles.size());
            for (File rf : rfFiles) {
                String processId = DroolsRuleFlowHelper.extractRuleFlowId(rf);
                if (processId != null) {
                    getSession().startProcess(processId);
                }
            }
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void cleanup() {
        outputWriter.printf("[DROOLS] Cleaning up...");
        // Close the session
        if (session != null) {
            if (droolsLogger != null) {
                droolsLogger.close();
            }
            String sessionStr = session.toString();
            session.dispose();
            outputWriter.printf("[DROOLS] Closed Drools session (%s)", sessionStr);
            session = null;
        }
        initialized = false;
    }

    protected StatefulKnowledgeSession getSession() {
        if (session == null) {
            session = knowledgeBase.newStatefulKnowledgeSession();
            outputWriter.printf("[DROOLS] Opened new Drools session (%s)", session.toString());
	    if(Engine.getInstance().getMode() != EngineMode.BENCHMARK) {
		droolsLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(session, "openske");
	    }
            DroolsAgendaEventListener agendaListener = new DroolsAgendaEventListener();
            // DroolsProcessEventListener processListener = new DroolsProcessEventListener(); 
            agendaListener.setOutputWriter(outputWriter);
            // processListener.setOutputWriter(outputWriter);
            session.addEventListener(agendaListener);
            // session.addEventListener(processListener);
        }
        return session;
    }

    public PrintWriter getOutputWriter() {
        return outputWriter;
    }

    public void setOutputWriter(PrintWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
