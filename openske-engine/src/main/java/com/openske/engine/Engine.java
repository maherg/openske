package com.openske.engine;

import java.io.File;
import java.io.PrintWriter;

import com.openske.drools.DroolsFacade;
import com.openske.nessus.NessusFileParser;
/**
 * OpenSKE's Engine
 * 
 */
public class Engine {

    protected static File currentWorkingDirectory;
    protected static Engine instance;
    protected DroolsFacade drools;
    protected boolean started;
    protected PrintWriter outputWriter;
    protected long startTime;
    protected EngineMode mode = EngineMode.NORMAL;
    protected File infrastructureFile;
    protected File nessusFile;
    protected NessusFileParser nessusParser;

    public Engine() {
        // Engine Initialization
        outputWriter = new PrintWriter(System.out, true);
        // Drools Initialization
        drools = new DroolsFacade();
        drools.setOutputWriter(outputWriter);
        Engine.currentWorkingDirectory = new File(".");
        nessusParser = new NessusFileParser();
        // Mark engine as not started yet
        started = false;
        Engine.instance = this;
    }
    
    public static Engine getInstance() {
        if(Engine.instance == null) {
            Engine.instance = new Engine();
        }
        return Engine.instance;
    }

    public void start() {
        if (this.isStarted()) {
            outputWriter.printf("[OPENSKE] OpenSKE's engine is already started !");
            return;
        } else {
            this.startTime = System.currentTimeMillis();
            outputWriter.printf("[OPENSKE] Starting OpenSKE engine in '%s' mode...", mode.toString().toLowerCase());
            try {
                // Marking the engine as started from the beginning,
                // since it's running in it's own thread
                started = true;
                
                // Drools Initialization
                drools.initialize();
                outputWriter.printf("[OPENSKE] Drools initialization completed at : %.2f seconds", this.getRunningTime());
                
                // Loading rules
                drools.loadRules();
                outputWriter.printf("[OPENSKE] Loading rules into Drools completed at : %.2f seconds", this.getRunningTime());
                
                // Loading facts
                drools.loadFacts(new String[] { infrastructureFile.getName() });
                outputWriter.printf("[OPENSKE] Loading facts into Drools completed at : %.2f seconds", this.getRunningTime());
                
                // Fire all activations
                drools.fireRules();
                outputWriter.printf("[OPENSKE] Firing rules completed at : %.2f seconds", this.getRunningTime());
		
                outputWriter.printf("[OPENSKE] Engine took in total : %.2f seconds !", this.getRunningTime());
            } catch (Throwable t) {
                t.printStackTrace(outputWriter);
                this.stop();
            }
        }
    }
    
    public void stop() {
        if (this.isStarted()) {
            outputWriter.printf("[OPENSKE] Stopping OpenSKE engine...");
            drools.cleanup();
            started = false;
        }
    }

    public boolean isStarted() {
        return started;
    }

    public PrintWriter getOutputWriter() {
        return outputWriter;
    }
    
    public static void print(String text, Object... args) {
        Engine.getInstance().getOutputWriter().printf(text, args);
    }

    public void setOutputWriter(PrintWriter outputWriter) {
        this.outputWriter = outputWriter;
        // Change Drools output writer as well
        drools.setOutputWriter(outputWriter);
    }

    public float getRunningTime() {
        if (this.isStarted()) {
            return (float) ((System.currentTimeMillis() - this.startTime) / 1000.0);
        } else {
            return 0L;
        }
    }

    public static File currentWorkingDirectory() {
        return currentWorkingDirectory;
    }

    public EngineMode getMode() {
        return mode;
    }

    public void setMode(EngineMode mode) {
        this.mode = mode;
    }

    public void setInfrastructureFile(File infrastructureFile) {
        this.infrastructureFile = infrastructureFile;
    }

    public void setNessusFile(File nessusFile) {
        this.nessusFile = nessusFile;
    }
}
