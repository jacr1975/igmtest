package com.example.igmtest.controller;

import java.util.Random;

public class Backoff {

    public static int defaultRetries = 4;
    public static long defaultWaitTimeInMills = 100;
    private int nTriesLeft;
    private long tToWait;

    private final Random random = new Random();

    public Backoff() {

        this(defaultRetries, defaultWaitTimeInMills);
    }

    public Backoff(int numberOfRetries, long defaultTimeToWait) {
        this.nTriesLeft = numberOfRetries;
        this.tToWait = defaultTimeToWait;
    }
    
    
    

    public static int getDefaultRetries() {
        return defaultRetries;
    }

    public static void setDefaultRetries(int defaultRetries) {
        Backoff.defaultRetries = defaultRetries;
    }

    public static long getDefaultWaitTimeInMills() {
        return defaultWaitTimeInMills;
    }

    public static void setDefaultWaitTimeInMills(long defaultWaitTimeInMills) {
        Backoff.defaultWaitTimeInMills = defaultWaitTimeInMills;
    }
    
    

    public boolean shouldRetry() {
        return nTriesLeft > 0;
    }
    
        public long getTimeToWait() {
        return this.tToWait;
    }

    public void doNotRetry() {
        nTriesLeft = 0;
    }

    public void error() {

        nTriesLeft--;
        if (!shouldRetry()) {
            System.err.println("*** RETRY FAILED ***");
        }
        waitUntilNextTry();
        tToWait += random.nextInt(100);
    }

    private void waitUntilNextTry() {

        try {
            Thread.sleep(tToWait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
