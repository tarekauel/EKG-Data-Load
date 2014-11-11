package core;

import infoobject.InfoObject;

import java.util.ArrayList;

/**
 * Created by Lars on 11.11.2014.
 */
public class Core {
    private static Core singletonInstance = null;
    private final int countWorker = 1;
    private ArrayList<InfoObject> listOfFinishedInfoObjects = new ArrayList<InfoObject>();
    private ArrayList<InfoObject> listOfScheduledInfoObjects = new ArrayList<InfoObject>();
    private ArrayList<Worker> listOfWorker = new ArrayList<Worker>();

    public Core() {
        singletonInstance = this;

        for (int i = 0; i < countWorker; i++) {
            listOfWorker.add(new Worker(i));
        }
    }

    public static Core getCore() {
        if (singletonInstance == null) {
            new Core();
        }
        return singletonInstance;
    }

    public synchronized void addInfoObjectToScheduleList(InfoObject object) {
        if (listOfScheduledInfoObjects.contains(object)) {
            return;
        }
        listOfScheduledInfoObjects.add(object);
    }

    public synchronized InfoObject getNextInfoObject() {
        if (listOfScheduledInfoObjects.size() < 1) {
            return null;
        }
        return listOfScheduledInfoObjects.remove(0);
    }

    public synchronized void addInfoObjectToFinished(InfoObject object) {
        listOfFinishedInfoObjects.add(object);
    }
}
