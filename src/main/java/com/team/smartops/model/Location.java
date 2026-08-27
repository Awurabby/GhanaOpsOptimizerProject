package com.team.smartops.model;

import java.util.Objects;

/**
 * Domain model for the {@code locations} table.
 * Fields match schema.sql: locationId, name, area, type, latitude, longitude.
 */
public class Location {

    private final int locationId;
    private final String name;
    private final String area;
    private final String type;
    private final double latitude;
    private final double longitude;

    public Location(int locationId, String name, String area,
                    String type, double latitude, double longitude) {
        this.locationId = locationId;
        this.name = name;
        this.area = area;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLocationId() { return locationId; }
    public String getName()    { return name; }
    public String getArea()    { return area; }
    public String getType()    { return type; }
    public double getLatitude()  { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public String toString() {
        return locationId + " - " + name + " (" + area + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location other)) return false;
        return locationId == other.locationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId);
    }
}
