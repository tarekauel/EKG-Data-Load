package core;

import infoobject.InfoObject;

import java.util.logging.Logger;

/**
 * This Thread can analyse an InfoObject via the analyse function provided by each InfoObject
 */
public class Worker extends Thread {
    public final int ID;
    private boolean stopRequest = false;
    private Logger log = Logger.getGlobal();

    /**
     * Creates a new WorkerThread which will be started immediately
     *
     * @param id the Identifier of the Thread (used for toString)
     */
    public Worker(int id) {
        this.ID = id;
        this.start();
    }

    /**
     * Will cause the Thread to stop after he finished the current InfoObject
     */
    public void stopThread() {
        stopRequest = true;
    }

    @Override
    public void run() {
        log.finest("Worker [" + this.toString() + "] is running.");
        //only do, while not stopped.
        while (!stopRequest) {
            //gets a new InfoObject and checks if its null.
            // If so, do nothing, if not, analyse it.
            InfoObject object = Core.getCore().getNextInfoObject();

            if (object == null) {
                //sending to yield. this should bring some performance because
                // other Threads can finish their work earlier.
                // This is caused by sleep(1000) means you will come back in 1 second.
                // Yield will continue as fast as the Thread can start again

                log.info(this.toString() + "says: No InfoObject to analyse.");
                currentThread().yield();
            } else {
                log.info(this.toString() + "says: Analysing InfoObject [" + object.toString() + "].");
                // Analyses the InfoObject
                object.analyse();
                // Marks the InfoObject as finished in the Core
                Core.getCore().addInfoObjectToFinished(object);
            }
        }
        log.info(this.toString() + "says: Stopped");
    }

    @Override
    public String toString() {
        return "Thread[" + this.ID + "]";
    }
}
