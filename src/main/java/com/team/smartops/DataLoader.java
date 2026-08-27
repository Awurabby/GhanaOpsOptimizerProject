package com.team.smartops;

import com.team.smartops.db.*;
import com.team.smartops.model.Location;
import com.team.smartops.model.ModelAdapters;
import com.team.smartops.model.Road;
import com.team.smartops.structures.Graph;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataLoader {
    public static AppState loadEverything() {
        AppState state = new AppState();
        Connection conn = null;

        try {
            conn = DatabaseConnection.connect();
            SchemaSetup.setup(conn);
            System.out.println("Database connected.");
        } catch (Exception e) {
            System.out.println("FAILED to connect to database: " + e.getMessage());
            return state;
        }

        try {
            state.locations = new LocationRepository().findAll(conn);
            state.locationNameIndex = ModelAdapters.buildLocationNameIndex(state.locations);
            System.out.println("Loaded " + state.locations.size() + " locations.");
        } catch (Exception e) {
            System.out.println("Locations did not load (" + e.getMessage() + ").");
        }

        try {
            state.roads = new RoadRepository().findAll(conn);
            System.out.println("Loaded " + state.roads.size() + " roads.");
        } catch (Exception e) {
            System.out.println("Roads did not load (" + e.getMessage() + ").");
        }

        try {
            state.requests = new ServiceRequestRepository().findAll(conn);
            System.out.println("Loaded " + state.requests.size() + " service requests.");
        } catch (Exception e) {
            System.out.println("Service requests did not load (" + e.getMessage() + ").");
        }

        try {
            state.resources = new ResourceRepository().findAll(conn);
            System.out.println("Loaded " + state.resources.size() + " resources.");
        } catch (Exception e) {
            System.out.println("Resources did not load (" + e.getMessage() + ").");
        }

        try {
            if (state.locations != null && state.roads != null) {
                state.graph = buildGraphFromState(state.locations, state.roads);
                state.graphLoaded = true;
                System.out.println("Graph built: " + state.graph.getNumNodes() + " nodes.");
            }
        } catch (Exception e) {
            System.out.println("Graph did not build (" + e.getMessage() + ").");
        }

        state.dataLoaded = true;
        return state;
    }

    private static Graph buildGraphFromState(List<Location> locations, List<Road> roads) {
        List<String> names = new ArrayList<>();
        Map<Integer, Integer> idToIndex = new LinkedHashMap<>();

        int index = 0;
        for (Location loc : locations) {
            idToIndex.put(loc.getLocationId(), index);
            names.add(loc.getName());
            index++;
        }

        Graph graph = new Graph(names.size(), names.toArray(new String[0]));

        for (Road road : roads) {
            Integer fromIndex = idToIndex.get(road.getFromLocationId());
            Integer toIndex = idToIndex.get(road.getToLocationId());
            if (fromIndex != null && toIndex != null) {
                graph.addEdge(fromIndex, toIndex, road.getDistance());
            }
        }

        return graph;
    }
}