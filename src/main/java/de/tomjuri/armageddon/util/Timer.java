package de.tomjuri.armageddon.util;

public class Timer {
    private long endTime = System.currentTimeMillis();

    public void startTimer(long millis) {
        endTime = System.currentTimeMillis() + millis;
    }

    public boolean isDone() {
        return System.currentTimeMillis() >= endTime;
    }
}
