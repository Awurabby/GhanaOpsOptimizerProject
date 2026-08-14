package com.team.smartops.algorithms.optimisation;

/** One request paired with the resource assigned to serve it. */
public class Assignment {

    private final String requestId;
    private final String resourceId;
    private final double distance;

    public Assignment(String requestId, String resourceId, double distance) {
        this.requestId = requestId;
        this.resourceId = resourceId;
        this.distance = distance;
    }

    public String getRequestId() { return requestId; }
    public String getResourceId() { return resourceId; }
    public double getDistance() { return distance; }

    @Override
    public String toString() {
        return requestId + " -> " + resourceId + " (distance=" + distance + ")";
    }
}