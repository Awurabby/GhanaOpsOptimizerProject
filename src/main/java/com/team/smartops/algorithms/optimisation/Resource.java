package com.team.smartops.algorithms.optimisation;

/**
 * PLACEHOLDER model. Will swap for the real Resource class once available.
 * Field names line up with the resources table: resourceId, type,
 * homeLocation, capacity, availabilityStatus.
 */
public class Resource {

    private final String resourceId;
    private final String type;
    private final String homeLocation;
    private boolean available;

    public Resource(String resourceId, String type, String homeLocation, boolean available) {
        this.resourceId = resourceId;
        this.type = type;
        this.homeLocation = homeLocation;
        this.available = available;
    }

    public String getResourceId() { return resourceId; }
    public String getType() { return type; }
    public String getHomeLocation() { return homeLocation; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return resourceId + "@" + homeLocation + (available ? " [free]" : " [busy]");
    }
}