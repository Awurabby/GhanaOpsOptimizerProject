package com.team.smartops.performance;

/**
 * OWNER: Team G.
 * Wraps timing for any algorithm run (nanoTime), feeds AlgorithmRunRepository.
 */
public class Timer {


    public static long timeInNanos(Runnable task) {

        long start = System.nanoTime();

        task.run();

        long end = System.nanoTime();

        return end - start;
    }

}