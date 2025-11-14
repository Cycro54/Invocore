package invoker54.invocore.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModLogger {
    private final Logger myLogger;
    private final AtomicBoolean debugMode;
    private double counter = 0;
    private double lastTime = 0;
    private static final List<ModLogger> loggers = new ArrayList<>();

    private ModLogger(Logger myLogger, AtomicBoolean debugMode) {
        this.myLogger = myLogger;
        this.debugMode = debugMode;
        this.lastTime = System.nanoTime();
    }

    public static ModLogger getLogger(Class<?> modClass, AtomicBoolean debugMode){
        ModLogger newLogger = new ModLogger(LogManager.getLogger(modClass), debugMode);
        loggers.add(newLogger);
        return newLogger;
    }

    public void debug(String s){
        if (!this.debugMode.get()) return;
        this.myLogger.debug(s);
    }

    public void info(String s){
        if (!this.debugMode.get()) return;
        this.myLogger.info(s);
    }

    public void warn(String s){
        if (!this.debugMode.get()) return;
        this.myLogger.warn(s);
    }

    public void error(String s){
        if (!this.debugMode.get()) return;
        this.myLogger.error(s);
    }

    public void resetTime(){
        this.counter = 0;
        this.lastTime = System.nanoTime();
    }

    public void timePassed(boolean resetTime){
        if (!this.debugMode.get()) return;
        this.myLogger.info(this.myLogger.getName() + " Time passed: " + ((System.nanoTime() - this.lastTime)/1000000000F));
        if (resetTime) this.resetTime();
    }

    public static void getAllTimePassed(){
        double totalTime = 0;
        for (ModLogger logger : loggers){
            totalTime += logger.counter;
            logger.timePassed(true);
        }
        loggers.get(0).error("Total time passed: " + (totalTime/1000000000F));
    }
}
