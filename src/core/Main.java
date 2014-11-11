package core;

import infoobject.WikiArticle;

/**
 * Created by Lars on 11.11.2014.
 */
public class Main {
    public static void main(String args[]) {
        Log.setOutput(true);
        Core.getCore().addInfoObjectToScheduleList(new WikiArticle("Computer network"));
    }
}
