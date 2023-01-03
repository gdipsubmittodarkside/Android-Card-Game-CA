package iss.workshop.android_ca;

import java.util.TimerTask;

public class Stopwatch extends TimerTask {
    public final long startTime;
    public long currentTime;

    public Stopwatch(){
        startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {

    }

    public long getCurrentTime() {
        currentTime = System.currentTimeMillis();
        return currentTime;
    }

    public long ElapsedTime(){
        long elapsedTime = currentTime - startTime;
        return elapsedTime;
    }

}
