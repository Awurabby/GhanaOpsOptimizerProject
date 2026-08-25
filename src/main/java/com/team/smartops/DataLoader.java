package com.team.smartops;

import com.team.smartops.db.*;
import java.sql.Connection;
import java.util.List;

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
            List<String> locations = new LocationRepository().findAll(conn);
            state.locations = locations;
            System.out.println("Loaded " + locations.size() + " locations.");
        } catch (Exception e) {
            System.out.println("Locations did not load (" + e.getMessage() + ").");
        }

        state.dataLoaded = true;
        return state;
    }
}