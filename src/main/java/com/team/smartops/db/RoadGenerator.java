package com.team.smartops.db;

import com.team.smartops.model.Location;
import com.team.smartops.model.Road;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

/**
 * Generates a fully connected road network for campus locations using
 * Haversine geographic distances between their GPS coordinates.
 *
 * Preserves all curated roads from {@code roads.csv} and connects the
 * remaining isolated campus locations so that graph algorithms (BFS, DFS,
 * Dijkstra, Prim, Kruskal) work across the entire University of Ghana campus.
 */
public class RoadGenerator {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final int K_NEAREST_NEIGHBORS = 3;

    /**
     * Calculates Haversine straight-line distance in kilometres between two
     * latitude/longitude coordinate points.
     */
    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = EARTH_RADIUS_KM * c;

        // Round to 2 decimal places, minimum 0.1 km
        double rounded = Math.round(dist * 100.0) / 100.0;
        return Math.max(0.1, rounded);
    }

    /**
     * Builds a list of connected roads for all locations.
     *
     * @param locations list of all campus locations
     * @param existingRoads list of curated initial roads (from roads.csv)
     * @return full list of roads ensuring all locations are connected
     */
    public static List<Road> generateConnectedRoads(List<Location> locations, List<Road> existingRoads) {
        List<Road> result = new ArrayList<>();
        Set<String> edgeSet = new HashSet<>();

        // 1. Include existing roads
        if (existingRoads != null) {
            for (Road r : existingRoads) {
                int u = r.getFromLocationId();
                int v = r.getToLocationId();
                String key = makeEdgeKey(u, v);
                if (edgeSet.add(key)) {
                    result.add(r);
                }
            }
        }

        if (locations == null || locations.size() <= 1) {
            return result;
        }

        Map<Integer, Location> locMap = new LinkedHashMap<>();
        for (Location loc : locations) {
            locMap.put(loc.getLocationId(), loc);
        }

        // 2. Connect each location to its K nearest neighbors
        for (Location loc : locations) {
            List<LocationDistance> distances = new ArrayList<>();
            for (Location other : locations) {
                if (loc.getLocationId() == other.getLocationId()) continue;
                double dist = haversineDistance(
                        loc.getLatitude(), loc.getLongitude(),
                        other.getLatitude(), other.getLongitude());
                distances.add(new LocationDistance(other.getLocationId(), dist));
            }

            distances.sort(Comparator.comparingDouble(ld -> ld.distance));

            int toAdd = Math.min(K_NEAREST_NEIGHBORS, distances.size());
            for (int i = 0; i < toAdd; i++) {
                LocationDistance ld = distances.get(i);
                int u = loc.getLocationId();
                int v = ld.locationId;
                String key = makeEdgeKey(u, v);
                if (edgeSet.add(key)) {
                    double dist = ld.distance;
                    double travelTime = Math.round((dist / 0.05) * 10.0) / 10.0; // ~3 km/h
                    result.add(new Road(u, v, dist, travelTime, 1.0));
                }
            }
        }

        return result;
    }

    /**
     * Reads locations from CSV, combines existing roads with generated nearest-neighbor
     * roads, and writes to {@code data/csv/roads_full.csv}.
     */
    public static void generateAndSaveFullRoads(String locationsCsvPath,
                                                String existingRoadsCsvPath,
                                                String outputCsvPath) throws IOException {
        List<Location> locations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(locationsCsvPath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    locations.add(new Location(
                            Integer.parseInt(parts[0].trim()),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            Double.parseDouble(parts[4].trim()),
                            Double.parseDouble(parts[5].trim())));
                }
            }
        }

        List<Road> existingRoads = new ArrayList<>();
        if (Files.exists(Path.of(existingRoadsCsvPath))) {
            try (BufferedReader br = new BufferedReader(new FileReader(existingRoadsCsvPath))) {
                br.readLine(); // skip header
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isBlank()) continue;
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        existingRoads.add(new Road(
                                Integer.parseInt(parts[0].trim()),
                                Integer.parseInt(parts[1].trim()),
                                Double.parseDouble(parts[2].trim()),
                                Double.parseDouble(parts[3].trim()),
                                Double.parseDouble(parts[4].trim())));
                    }
                }
            }
        }

        List<Road> fullRoads = generateConnectedRoads(locations, existingRoads);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputCsvPath))) {
            bw.write("fromLocationId,toLocationId,distance,travelTime,roadConditionWeight");
            bw.newLine();
            for (Road r : fullRoads) {
                bw.write(String.format(Locale.US, "%d,%d,%.2f,%.1f,%.1f",
                        r.getFromLocationId(), r.getToLocationId(),
                        r.getDistance(), r.getTravelTime(), r.getRoadConditionWeight()));
                bw.newLine();
            }
        }
    }

    private static String makeEdgeKey(int u, int v) {
        int min = Math.min(u, v);
        int max = Math.max(u, v);
        return min + "-" + max;
    }

    private static class LocationDistance {
        int locationId;
        double distance;

        LocationDistance(int locationId, double distance) {
            this.locationId = locationId;
            this.distance = distance;
        }
    }
}
