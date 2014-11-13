package core;

import infoobject.WikiArticle;

/**
 * Can start the program
 */
public class Main {
    /**
     * Starts the program
     *
     * @param args no args used!
     */
    public static void main(String args[]) {
        //adds an start InfoObject to the Core. This article will fill up the working queue.
        Core.getCore().addWikiArticleToScheduleList(new WikiArticle("Computer network"));
    }
}
