package com.openske.engine;

import java.io.File;
import java.io.PrintWriter;

import com.openske.drools.DroolsFacade;
/**
 * OpenSKE's Engine
 * 
 */
public class Engine {

    protected DroolsFacade drools;
    protected boolean started;
    protected PrintWriter outputWriter;
    protected long runningTime;
    protected static File currentWorkingDirectory;
    protected static Engine instance;

    public Engine() {
        // Engine Initialization
        outputWriter = new PrintWriter(System.out, true);
        // Drools Initialization
        drools = new DroolsFacade();
        drools.setOutputWriter(outputWriter);
        Engine.currentWorkingDirectory = new File(".");
        // Mark engine as not started yet
        started = false;
        Engine.instance = this;
    }
    
    public static Engine getInstance() {
        return Engine.instance;
    }

    public void run() {
        if (this.isStarted()) {
            outputWriter.format("[OPENSKE] OpenSKE's engine is already running");
            return;
        } else {
            outputWriter.format("[OPENSKE] Running OpenSKE engine...");
            try {
                // Reset the runningTime
                this.runningTime = 0L;
                long startTime = System.currentTimeMillis();
                // Marking the engine as started from the beginning,
                // since it's running in it's own thread
                started = true;
                drools.initialize();
                drools.loadRules();
                drools.loadFacts();
                // Disabling the process running for now
                // drools.loadProcesses();
                // drools.startProcesses();
                drools.fireRules();
                long endTime = System.currentTimeMillis();
                this.runningTime = endTime - startTime;
                outputWriter.format("[OPENSKE] Engine took %.2f seconds !", this.getRunningTime());
            } catch (Throwable t) {
                t.printStackTrace(outputWriter);
                this.stop();
            }
        }
    }

    public void stop() {
        if (this.isStarted()) {
            outputWriter.format("[OPENSKE] Stopping OpenSKE engine...");
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
        Engine.getInstance().getOutputWriter().format(text, args);
    }

    public void setOutputWriter(PrintWriter outputWriter) {
        this.outputWriter = outputWriter;
        // Change Drools output writer as well
        drools.setOutputWriter(outputWriter);
    }

    public float getRunningTime() {
        if (this.isStarted()) {
            return (float) ((float) runningTime / 1000.0);
        } else {
            return 0L;
        }
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.run();
        engine.stop();
    }

    public static File currentWorkingDirectory() {
        return currentWorkingDirectory;
    }
}
