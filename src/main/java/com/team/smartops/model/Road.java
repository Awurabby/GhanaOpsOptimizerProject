package com.team.smartops.model;

/**
 * Domain model for the {@code roads} table.
 * Fields match schema.sql: fromLocationId, toLocationId, distance,
 * travelTime, roadConditionWeight.
 */
public class Road {

    private final int fromLocationId;
    private final int toLocationId;
    private final double distance;
    private final double travelTime;
    private final double roadConditionWeight;

    public Road(int fromLocationId, int toLocationId, double distance,
                double travelTime, double roadConditionWeight) {
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distance = distance;
        this.travelTime = travelTime;
        this.roadConditionWeight = roadConditionWeight;
    }

    public int getFromLocationId()       { return fromLocationId; }
    public int getToLocationId()         { return toLocationId; }
    public double getDistance()           { return distance; }
    public double getTravelTime()        { return travelTime; }
    public double getRoadConditionWeight() { return roadConditionWeight; }

    @Override
    public String toString() {
        return fromLocationId + " -> " + toLocationId
                + " | Distance: " + distance + "km";
    }
}
