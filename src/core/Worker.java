package core;

import infoobject.InfoObject;

/**
 * This Thread can analyse an InfoObject via the analyse function provided by each InfoObject
 */
public class Worker extends Thread {
    public final int ID;
    private boolean stopRequest = false;

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
        //only do, while not stopped.
        while (!stopRequest) {
            //gets a new InfoObject and checks if its null.
            // If so, do nothing, if not, analyse it.
            InfoObject object = Core.getCore().getNextInfoObject();

            if (object == null) {
                //sending to yield. this should bring some performance because
                // other Threads can finish their work earlier.
                // This is caused by sleep(1000) means busy waiting in most cases.
                currentThread().yield();
            } else {
                // Analyses the InfoObject
                object.analyse();
                // Marks the InfoObject as finished in the Core
                Core.getCore().addInfoObjectToFinished(object);
            }
        }
    }

    @Override
    public String toString() {
        return "Thread[" + this.ID + "]";
    }
}
