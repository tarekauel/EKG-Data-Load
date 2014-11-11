package core;

import infoobject.InfoObject;

/**
 * Created by Lars on 11.11.2014.
 */
public class Worker extends Thread {
    public final int ID;
    private boolean stopRequest = false;

    public Worker(int id) {
        this.ID = id;
        this.start();
    }

    public void stopThread() {
        stopRequest = true;
    }

    @Override
    public void run() {
        while (!stopRequest) {
            InfoObject object = Core.getCore().getNextInfoObject();
            if (object == null) {
                //Log.log("No Work at the moment." + this.toString() + " going to yield");
                currentThread().yield();
            } else {
                Log.log("Analysing:" + object.toString());
                object.analyse();
                Log.log("Finished:" + object.toString());
                Core.getCore().addInfoObjectToFinished(object);
            }
        }
    }

    @Override
    public String toString() {
        return "Thread[" + this.ID + "]";
    }
}
