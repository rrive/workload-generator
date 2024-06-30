package util;

public class SimpleLog {

    public static void println(String str) {
        System.out.println(str);
    }

    public static void printError(Exception e) {
        e.printStackTrace();
    }

    public static void printError(Exception e, String msg) {
        println(msg);
        printError(e);
    }
}
