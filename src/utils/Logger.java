package utils;

public class Logger {
    private static String messageHead = "Logger";
    public Logger() { }

    public static void log(Object classObject, String msg) {
        System.out.printf("[%s: %s: %s]\n", messageHead, classObject, msg);
    }
}