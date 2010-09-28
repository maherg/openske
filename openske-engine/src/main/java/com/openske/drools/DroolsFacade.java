package com.openske.drools;

import java.io.File;
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

import com.openske.model.assets.data.DatabaseAsset;
import com.openske.model.assets.data.FileAsset;
import com.openske.model.assets.services.ServiceAsset;
import com.openske.model.hardware.Host;
import com.openske.model.hardware.Router;
import com.openske.model.security.User;
import com.openske.model.security.UserAccount;
import com.openske.model.software.Software;

public class DroolsFacade {

    protected KnowledgeBase knowledgeBase;
    protected PrintWriter outputWriter;
    protected StatefulKnowledgeSession session;
    protected KnowledgeRuntimeLogger logger;
    protected boolean initialized;

    public void initialize() {
        if (!isInitialized()) {
            outputWriter.format("Initializing Drools Knowledge Base...");
            knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
            initialized = true;
        }
    }

    public void loadRules() throws Throwable {
        if (isInitialized()) {
            // Search for DRL files starting from the current directory
            Collection<File> drlFiles = DroolsResourceHelper.listResources(ResourceType.DRL);
            outputWriter.format("Loading %d rules...", drlFiles.size());
            // Load the found DRL files into the KnowledgeBuilder
            KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            for (File drl : drlFiles) {
                outputWriter.format("\t - Loading '%s' ...", drl.getPath());
                knowledgeBuilder.add(ResourceFactory.newFileResource(drl.getAbsolutePath()), ResourceType.DRL);
                if (knowledgeBuilder.hasErrors()) {
                    outputWriter.format(knowledgeBuilder.getErrors().toString());
                    throw new RuntimeException("Found errors in DRL file : " + drl);
                }
            }

            Collection<KnowledgePackage> knowledgePackages = knowledgeBuilder.getKnowledgePackages();
            outputWriter.format("Adding %d compiled knowledge packages to the knowledgebase...", knowledgePackages.size());
            for (KnowledgePackage kpackage : knowledgePackages) {
                outputWriter.format("\t - Knowledge Package %s (%d rules)", kpackage.getName(), kpackage.getRules().size());
            }
            knowledgeBase.addKnowledgePackages(knowledgePackages);
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void loadFacts() {
        if (isInitialized()) {
            // Hosts
            Host webHost = new Host("web.proggy");
            Host dbHost = new Host("database.proggy");
            Host station1 = new Host("station1.proggy");
            Host station2 = new Host("station2.proggy");
            Host attackerHost = new Host("attacker.proggy");
            Router router = new Router("router.proggy");
            // Connections
            router.addConnection(webHost);
            router.addConnection(dbHost);
            router.addConnection(station1);
            router.addConnection(station2);
            webHost.addConnection(attackerHost);
            // Software
            Software phpMyAdmin = new Software("phpmyadmin", "phpmyadmin", "2.1", webHost).addVulnerability("CVE-2008-2223");
            Software apache = new Software("apache", "apache", "2.2", webHost);
            Software mysql = new Software("mysql", "mysql", "4.1", dbHost).addVulnerability("CVE-2003-1480");
            Software proggyWeb = new Software("proggysolutions", "proggyweb", "1.0", webHost);
            webHost.addSoftware(apache);
            webHost.addSoftware(phpMyAdmin);
            dbHost.addSoftware(mysql);
            // Assets
            ServiceAsset proggyBookWeb = new ServiceAsset("Proggy Web", proggyWeb);
            DatabaseAsset proggyBookDatabase = new DatabaseAsset("ProggyBook Database", dbHost, "proggybook");
            FileAsset proggyBookDesign = new FileAsset("ProggyBook Design", station1, "/home/proggy/design.png");
            dbHost.addAsset(proggyBookDatabase);
            webHost.addAsset(proggyBookWeb);
            station1.addAsset(proggyBookDesign);
            // User Accounts
            UserAccount phpMyAdminAccount = new UserAccount("admin", "admin", phpMyAdmin);
            UserAccount rootAccount = new UserAccount("root", "root", mysql);
            // Users
            User attacker = new User("Mr. Attacker", "mr.attacker@attackers.net", attackerHost, true);
            // Insertion
            getSession().insert(router);
            getSession().insert(webHost);
            getSession().insert(dbHost);
            getSession().insert(station1);
            getSession().insert(station2);
            getSession().insert(phpMyAdmin);
            getSession().insert(apache);
            getSession().insert(mysql);
            getSession().insert(proggyBookWeb);
            getSession().insert(proggyBookDatabase);
            getSession().insert(proggyBookDesign);
            getSession().insert(phpMyAdminAccount);
            getSession().insert(rootAccount);
            getSession().insert(attacker);
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void loadProcesses() {
        if (isInitialized()) {
            Collection<File> rfFiles = DroolsResourceHelper.listResources(ResourceType.DRF);
            outputWriter.format("Loading %d processes...", rfFiles.size());
            KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            for (File rf : rfFiles) {
                knowledgeBuilder.add(ResourceFactory.newFileResource(rf), ResourceType.DRF);
                if(knowledgeBuilder.hasErrors()) {
                    outputWriter.format(knowledgeBuilder.getErrors().toString());
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
            outputWriter.format("Firing all rules...");
            int rulesFired = getSession().fireAllRules();
            outputWriter.format("Fired %d rules", rulesFired);
        } else {
            // TODO : Handle calling this method without being initialized
        }
    }

    public void startProcesses() {
        if (isInitialized()) {
            Collection<File> rfFiles = DroolsResourceHelper.listResources(ResourceType.DRF);
            outputWriter.format("Starting %d processes...", rfFiles.size());
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
        outputWriter.format("Cleaning up...");
        // Close the session
        if (session != null) {
            if (logger != null) {
                logger.close();
            }
            String sessionStr = session.toString();
            session.dispose();
            outputWriter.format("Closed Drools session (%s)", sessionStr);
            session = null;
        }
        initialized = false;
    }

    protected StatefulKnowledgeSession getSession() {
        if (session == null) {
            session = knowledgeBase.newStatefulKnowledgeSession();
            outputWriter.format("Opened new Drools session (%s)", session.toString());
            logger = KnowledgeRuntimeLoggerFactory.newFileLogger(session, "openske");
            DroolsAgendaEventListener agendaListener = new DroolsAgendaEventListener();
            DroolsProcessEventListener processListener = new DroolsProcessEventListener(); 
            agendaListener.setOutputWriter(outputWriter);
            processListener.setOutputWriter(outputWriter);
            session.setGlobal("output", outputWriter);
            session.addEventListener(agendaListener);
            session.addEventListener(processListener);
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
