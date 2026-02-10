package openske.drools;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.logger.KieLoggers;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import openske.engine.Engine;
import openske.engine.EngineMode;
import openske.model.Infrastructure;
import openske.model.InfrastructureItem;
import openske.model.software.Software;
import openske.model.software.Weakness;


public class DroolsFacade {
    
    protected KieBase kieBase;
    protected PrintWriter outputWriter;
    protected KieSession kieSession;
    protected KieRuntimeLogger kieLogger;
    protected boolean initialized;

    public void initialize() {
        if (!isInitialized()) {
            log("Initializing KIE Knowledge Base...");
            initialized = true;
        }
    }
    
    public void log(String text, Object... args) {
        outputWriter.printf("[DROOLS] " + text, args);
    }

    public void loadRules() throws Throwable {
        if (isInitialized()) {
            // Search for DRL files starting from the current directory
            Collection<File> drlFiles = DroolsResourceHelper.listResources("drl");
            log("Loading %d rule files...", drlFiles.size());
            
            // Use KieServices to build the knowledge base
            KieServices kieServices = KieServices.Factory.get();
            KieFileSystem kfs = kieServices.newKieFileSystem();
            
            // Load the found DRL files into the KieFileSystem
            for (File drl : drlFiles) {
                log("\t - Loading '%s' ...", drl.getPath());
                kfs.write(ResourceFactory.newFileResource(drl.getAbsolutePath()));
            }
            
            // Build the KieContainer
            KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
            Results results = kieBuilder.getResults();
            
            if (results.hasMessages(Message.Level.ERROR)) {
                log(results.getMessages().toString());
                throw new RuntimeException("Found errors in DRL files");
            }
            
            kieBase = kieServices.newKieContainer(kieBuilder.getKieModule().getReleaseId()).getKieBase();
            
            Collection<KiePackage> kiePackages = kieBase.getKiePackages();
            log("Adding %d compiled knowledge packages to the knowledgebase...", kiePackages.size());
            for (KiePackage kpackage : kiePackages) {
                log("\t - Knowledge Package %s (%d rules)", kpackage.getName(), kpackage.getRules().size());
            }
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
            Collection<File> rfFiles = DroolsResourceHelper.listResources("rf");
            log("Loading %d processes...", rfFiles.size());
            
            KieServices kieServices = KieServices.Factory.get();
            KieFileSystem kfs = kieServices.newKieFileSystem();
            
            for (File rf : rfFiles) {
                kfs.write(ResourceFactory.newFileResource(rf));
            }
            
            KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
            Results results = kieBuilder.getResults();
            
            if (results.hasMessages(Message.Level.ERROR)) {
                log(results.getMessages().toString());
                throw new RuntimeException("Found errors in RF files");
            }
            
            // Note: In modern Drools, processes are handled differently
            // For now, we just rebuild the kbase
            kieBase = kieServices.newKieContainer(kieBuilder.getKieModule().getReleaseId()).getKieBase();
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
            Collection<File> rfFiles = DroolsResourceHelper.listResources("rf");
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
        if (kieSession != null) {
            if (kieLogger != null) {
                kieLogger.close();
            }
            String sessionStr = kieSession.toString();
            kieSession.dispose();
            log("Closed KIE session (%s)", sessionStr);
            kieSession = null;
        }
        initialized = false;
    }

    protected KieSession getSession() {
        if (kieSession == null) {
            kieSession = kieBase.newKieSession();
            log("Opened new KIE session (%s)", kieSession.toString());
	    if(Engine.getInstance().getMode() != EngineMode.BENCHMARK) {
		kieLogger = KieServices.Factory.get().getLoggers().newFileLogger(kieSession, "openske-drools");
	    }
            DroolsAgendaEventListener agendaListener = new DroolsAgendaEventListener();
            // DroolsProcessEventListener processListener = new DroolsProcessEventListener(); 
            agendaListener.setOutputWriter(outputWriter);
            // processListener.setOutputWriter(outputWriter);
            kieSession.addEventListener(agendaListener);
            // kieSession.addEventListener(processListener);
        }
        return kieSession;
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
