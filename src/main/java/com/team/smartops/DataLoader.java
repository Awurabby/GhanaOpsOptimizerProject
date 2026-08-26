package com.team.smartops;

import com.team.smartops.db.*;
import com.team.smartops.structures.Graph;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            System.out.println("Database connected.");
        } catch (Exception e) {
            System.out.println("FAILED to connect to database: " + e.getMessage());
            return state;
        }

       try {
            state.locations = new LocationRepository().findAll(conn);
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
            state.graph = buildGraph(conn);
            state.graphLoaded = true;
            System.out.println("Graph built: " + state.graph.getNumNodes() + " nodes.");
        } catch (Exception e) {
            System.out.println("Graph did not build (" + e.getMessage() + ").");
        }

        state.dataLoaded = true;
        return state;
    }

    private static Graph buildGraph(Connection conn) throws Exception {
        List<String> names = new ArrayList<>();
        Map<Integer, Integer> idToIndex = new LinkedHashMap<>();

        PreparedStatement locStmt = conn.prepareStatement("SELECT locationId, name FROM locations");
        ResultSet locRs = locStmt.executeQuery();
        int index = 0;
        while (locRs.next()) {
            int id = locRs.getInt("locationId");
            String name = locRs.getString("name");
            idToIndex.put(id, index);
            names.add(name);
            index++;
        }
        Graph graph = new Graph(names.size(), names.toArray(new String[0]));

        PreparedStatement roadStmt = conn.prepareStatement(
            "SELECT fromLocationId, toLocationId, distance FROM roads");
        ResultSet roadRs = roadStmt.executeQuery();
        while (roadRs.next()) {
            int fromId = roadRs.getInt("fromLocationId");
            int toId = roadRs.getInt("toLocationId");
            double distance = roadRs.getDouble("distance");

            Integer fromIndex = idToIndex.get(fromId);
            Integer toIndex = idToIndex.get(toId);
            if (fromIndex != null && toIndex != null) {
                graph.addEdge(fromIndex, toIndex, distance);
            }
        }
         return graph;
    }
}