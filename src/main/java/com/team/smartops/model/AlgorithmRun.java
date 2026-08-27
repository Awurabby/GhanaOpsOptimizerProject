package com.team.smartops.model;

/**
 * Domain model for the {@code algorithm_runs} table.
 * Fields match schema.sql: runId, algorithmName, inputSize, timeNs,
 * memoryKb, dateRun.
 */
public class AlgorithmRun {

    private final int runId;
    private final String algorithmName;
    private final int inputSize;
    private final long timeNs;
    private final int memoryKb;
    private final String dateRun;

    public AlgorithmRun(int runId, String algorithmName, int inputSize,
                        long timeNs, int memoryKb, String dateRun) {
        this.runId = runId;
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.timeNs = timeNs;
        this.memoryKb = memoryKb;
        this.dateRun = dateRun;
    }

    public int getRunId()           { return runId; }
    public String getAlgorithmName() { return algorithmName; }
    public int getInputSize()       { return inputSize; }
    public long getTimeNs()         { return timeNs; }
    public int getMemoryKb()        { return memoryKb; }
    public String getDateRun()      { return dateRun; }

    @Override
    public String toString() {
        return runId + " - " + algorithmName
                + " | Time: " + timeNs + " ns";
    }
}
