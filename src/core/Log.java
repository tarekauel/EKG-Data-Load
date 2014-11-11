package core;

/**
 * Created by Lars on 11.11.2014.
 */
public class Log {
    private static boolean output = false;

    public static void setOutput(boolean b) {
        output = b;
    }

    public static synchronized void log(String logEntry) {
        if (output) {
            System.out.println(logEntry);
        }
    }

}
