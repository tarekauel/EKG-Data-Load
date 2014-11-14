package core;

import infoobject.WikiArticle;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Can start the program
 */
public class Main {
    private static Logger log = Logger.getGlobal();
    /**
     * Starts the program
     *
     * @param args no args used!
     */
    public static void main(String args[]) {
        log.setLevel(Level.OFF);

        // setting up the observer
        Observer.getObserver().setLogLevel(Level.INFO);
        Observer.getObserver().useLogger(false);
        Observer.getObserver().useSystemOut(true);
        Observer.getObserver().start();

        //adds an start InfoObject to the Core. This article will fill up the working queue.
        log.info("Logging Level is set to [" + log.getLevel() + "].");
        Core.getCore().addWikiArticleToScheduleList(new WikiArticle("Computer network"));
    }
}
