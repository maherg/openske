package openske.drools;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;

import openske.engine.Engine;
import openske.engine.EngineMode;
import openske.model.Infrastructure;
import openske.model.InfrastructureItem;
import openske.model.software.Software;
import openske.model.software.Weakness;

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
            log("Initializing Drools Knowledge Base...");
            knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
            initialized = true;
        }
    }
    
    public void log(String text, Object... args) {
        outputWriter.printf("[DROOLS] " + text, args);
    }

    public void loadRules() throws Throwable {
        if (isInitialized()) {
            // Search for DRL files starting from the current directory
            Collection<File> drlFiles = DroolsResourceHelper.listResources(ResourceType.DRL.getDefaultExtension());
            log("Loading %d rule files...", drlFiles.size());
            // Load the found DRL files into the KnowledgeBuilder
            KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            for (File drl : drlFiles) {
                log("\t - Loading '%s' ...", drl.getPath());
                knowledgeBuilder.add(ResourceFactory.newFileResource(drl.getAbsolutePath()), ResourceType.DRL);
                if (knowledgeBuilder.hasErrors()) {
                    log(knowledgeBuilder.getErrors().toString());
                    throw new RuntimeException("Found errors in DRL file : " + drl);
                }
            }

            Collection<KnowledgePackage> knowledgePackages = knowledgeBuilder.getKnowledgePackages();
            log("Adding %d compiled knowledge packages to the knowledgebase...", knowledgePackages.size());
            for (KnowledgePackage kpackage : knowledgePackages) {
                log("\t - Knowledge Package %s (%d rules)", kpackage.getName(), kpackage.getRules().size());
            }
            knowledgeBase.addKnowledgePackages(knowledgePackages);
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void loadFacts(Infrastructure infrastructure) {
        if (isInitialized()) {
            
            for(String type : infrastructure.types()) {
                for(Object fact : infrastructure.list(type)) {
                    getSession().insert(fact);
                }
            }
            
            // FIXME : Insert software weaknesses explicitly
            for(InfrastructureItem i : infrastructure.list(Software.class)) {
                for(Weakness w : ((Software)i).getWeaknesses()) {
                    getSession().insert(w);
                }
            }
            
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void loadProcesses() {
        if (isInitialized()) {
            Collection<File> rfFiles = DroolsResourceHelper.listResources(ResourceType.DRF.getDefaultExtension());
            log("Loading %d processes...", rfFiles.size());
            KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            for (File rf : rfFiles) {
                knowledgeBuilder.add(ResourceFactory.newFileResource(rf), ResourceType.DRF);
                if(knowledgeBuilder.hasErrors()) {
                    log(knowledgeBuilder.getErrors().toString());
                    throw new RuntimeException("Found errors in DRF file : " + rf);
                }
            }
            knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void fireRules() {
        if (isInitialized()) {
            log("Firing all rules...");
            getSession().fireAllRules();
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void startProcesses() {
        if (isInitialized()) {
            Collection<File> rfFiles = DroolsResourceHelper.listResources(ResourceType.DRF.getDefaultExtension());
            log("Starting %d processes...", rfFiles.size());
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
        log("Cleaning up...");
        // Close the session
        if (session != null) {
            if (droolsLogger != null) {
                droolsLogger.close();
            }
            String sessionStr = session.toString();
            session.dispose();
            log("Closed Drools session (%s)", sessionStr);
            session = null;
        }
        initialized = false;
    }

    protected StatefulKnowledgeSession getSession() {
        if (session == null) {
            session = knowledgeBase.newStatefulKnowledgeSession();
            log("Opened new Drools session (%s)", session.toString());
	    if(Engine.getInstance().getMode() != EngineMode.BENCHMARK) {
		droolsLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(session, "openske-drools");
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
