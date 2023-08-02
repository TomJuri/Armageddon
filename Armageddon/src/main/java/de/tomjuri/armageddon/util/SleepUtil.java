package de.tomjuri.armageddon.util;

public class SleepUtil {
    public static void sleep(long millis) {
        try { Thread.sleep(millis); } catch (InterruptedException ignored) { }
    }
}
