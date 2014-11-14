package core;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Observer can give some statistics
 */
public class Observer extends Thread {
    private static Observer singleton = new Observer();
    private boolean isRunning = false;
    private boolean requestedStop = false;
    private long totalFinishedInfoObjects = 0;
    private long lastFinishedInfoObjects = 0;
    private long plannedWikiArticles = 0;
    private int sleepSeconds = 1;
    private Logger log = Logger.getGlobal();
    private boolean useLogger = false;
    private boolean useSystemOut = false;
    private Level logLevel = Level.INFO;

    /**
     * prevent from creating multiple Observer
     */
    private Observer() {

    }

    /**
     * Gives you access to the Observer
     *
     * @return
     */
    public static Observer getObserver() {
        return singleton;
    }

    /**
     * starts the observer Thread, if it is not already started
     */
    public void startObserver() {
        if (!isRunning) {
            this.start();
            isRunning = true;
        }
    }

    /**
     * Requests the Observer to stop
     */
    public void stopObserver() {
        if (isRunning && !requestedStop) {
            requestedStop = true;
        }
    }

    /**
     * Gives you an answer about the Thread Status
     *
     * @return true if the Observer is running
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Sets the time until he gives again some informations
     *
     * @param seconds
     */
    public void setSleepTimeInSeconds(int seconds) {
        if (seconds > 1) {
            this.sleepSeconds = seconds;
        }
    }

    /**
     * using the global Logger for the information.
     * The logging level can be adjusted via function setLogLevel(level)
     * can be used together with useSystemOut
     *
     * @param useLogger true, if you want to use the logger
     */
    public void useLogger(boolean useLogger) {
        this.useLogger = useLogger;
    }

    /**
     * adjusts the loglevel for the Observer.
     * Default is INFO
     *
     * @param level
     */
    public void setLogLevel(Level level) {
        this.logLevel = level;
    }

    /**
     * using the System.out.println() for the information.
     * can be used together with useLogger
     *
     * @param useSystemOut true, if you want to use the System.out
     */
    public void useSystemOut(boolean useSystemOut) {
        this.useSystemOut = useSystemOut;
    }

    @Override
    public void run() {
        isRunning = true;
        while (!requestedStop) {

            long diff = totalFinishedInfoObjects - lastFinishedInfoObjects;
            lastFinishedInfoObjects = lastFinishedInfoObjects + diff;
            String logMessage = "[Observer] : PlannedWikiArticles [" + plannedWikiArticles +
                    "] \t Articles per time [" + diff +
                    "(obj/" + sleepSeconds + "secs)]";
            if (useLogger) {
                log.log(logLevel, logMessage);
            }
            if (useSystemOut) {
                System.out.println(logMessage);
            }
            // send Observer to sleep.
            try {
                sleep(sleepSeconds * 1000);
            } catch (InterruptedException e) {
                log.severe("Observer could not go to sleep");
            }
        }
        isRunning = false;
    }

    /**
     * Adds 1 Object to the counter
     */
    protected void incrementFinishedInfoObject() {
        totalFinishedInfoObjects++;
    }

    /**
     * Sets the currently planned WikiArticles
     *
     * @param plannedWikiArticles
     */
    public void setPlannedWikiArticles(int plannedWikiArticles) {
        this.plannedWikiArticles = plannedWikiArticles;
    }
}
