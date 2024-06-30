package util;

import java.util.concurrent.TimeUnit;

import static util.SimpleLog.println;

public class Time {

    public static void sleep(long seconds) throws InterruptedException {
        println("Waiting " + seconds + " seconds to copy files.");
        TimeUnit.SECONDS.sleep(seconds);
    }

}
