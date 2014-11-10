package core;


import api.Communicator;

/**
 * Created by Lars on 10.11.2014.
 */
public class Core {

    public Core(String startTitle) {
        new Communicator("http://en.wikipedia.org/w/api.php");
        Communicator.getCommunicator().requestInfo(startTitle);
    }
}
