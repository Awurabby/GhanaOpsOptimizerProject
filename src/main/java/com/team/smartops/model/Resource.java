package com.team.smartops.model;

import java.util.Objects;

/**
 * Domain model for the {@code resources} table.
 * Fields match schema.sql: resourceId, type, homeLocation, capacity,
 * availabilityStatus.
 */
public class Resource {

    private final int resourceId;
    private final String type;
    private final int homeLocation;
    private final int capacity;
    private String availabilityStatus;

    public Resource(int resourceId, String type, int homeLocation,
                    int capacity, String availabilityStatus) {
        this.resourceId = resourceId;
        this.type = type;
        this.homeLocation = homeLocation;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
    }

    public int getResourceId()          { return resourceId; }
    public String getType()             { return type; }
    public int getHomeLocation()        { return homeLocation; }
    public int getCapacity()            { return capacity; }
    public String getAvailabilityStatus() { return availabilityStatus; }

    public boolean isAvailable() {
        return "Available".equalsIgnoreCase(availabilityStatus);
    }

    public void setAvailabilityStatus(String status) {
        this.availabilityStatus = status;
    }

    @Override
    public String toString() {
        return resourceId + " - " + type
                + " | Status: " + availabilityStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource other)) return false;
        return resourceId == other.resourceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId);
    }
}
