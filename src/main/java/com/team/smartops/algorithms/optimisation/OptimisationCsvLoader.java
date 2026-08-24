package com.team.smartops.algorithms.optimisation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Converts Team B's CSV records into inputs owned by the optimisation module. */
public class OptimisationCsvLoader {

    public List<ServiceRequest> loadPendingRequests(Path requestsCsv, Path locationsCsv)
            throws IOException {
        Map<String, String> locationNames = loadLocationNames(locationsCsv);
        List<Map<String, String>> rows = readRows(requestsCsv);
        List<ServiceRequest> requests = new ArrayList<>();

        for (Map<String, String> row : rows) {
            if (!"Pending".equalsIgnoreCase(required(row, "status"))) {
                continue;
            }

            String requestId = required(row, "requestId");
            String urgencyText = required(row, "urgency");
            int urgency = urgencyScore(requestId, urgencyText);
            String category = required(row, "category");

            requests.add(new ServiceRequest(
                requestId,
                locationName(locationNames, required(row, "source")),
                locationName(locationNames, required(row, "destination")),
                category,
                urgency,
                estimatedCost(category),
                urgency * 10));
        }

        return requests;
    }

    private Map<String, String> loadLocationNames(Path locationsCsv) throws IOException {
        Map<String, String> names = new HashMap<>();
        for (Map<String, String> row : readRows(locationsCsv)) {
            names.put(required(row, "locationId"), required(row, "name"));
        }
        return names;
    }

    private int urgencyScore(String requestId, String urgency) {
        return switch (urgency.toLowerCase(Locale.ROOT)) {
            case "critical" -> 4;
            case "high" -> 3;
            case "medium" -> 2;
            case "low" -> 1;
            default -> throw new IllegalArgumentException(
                "unknown urgency for request " + requestId + ": " + urgency);
        };
    }

    private int estimatedCost(String category) {
        String normalized = category.toLowerCase(Locale.ROOT);

        if (normalized.contains("electrical")) return 5;
        if (normalized.contains("maintenance") || normalized.contains("repair")
                || normalized.contains("plumbing") || normalized.contains("water")) return 4;
        if (normalized.contains("security") || normalized.contains("internet")
                || normalized.contains("network") || normalized.contains("equipment")) return 3;
        if (normalized.contains("cleaning") || normalized.contains("inspection")
                || normalized.contains("complaint")) return 2;
        return 3;
    }

    private String locationName(Map<String, String> locationNames, String locationId) {
        String name = locationNames.get(locationId);
        if (name == null) {
            throw new IllegalArgumentException("unknown location ID: " + locationId);
        }
        return name;
    }

    private String required(Map<String, String> row, String field) {
        String value = row.get(field);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("missing CSV field: " + field);
        }
        return value.trim();
    }

    private List<Map<String, String>> readRows(Path csvPath) throws IOException {
        List<String> lines = Files.readAllLines(csvPath);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("CSV file is empty: " + csvPath);
        }

        List<String> headers = parseLine(lines.get(0));
        List<Map<String, String>> rows = new ArrayList<>();

        for (int lineNumber = 1; lineNumber < lines.size(); lineNumber++) {
            if (lines.get(lineNumber).isBlank()) continue;
            List<String> values = parseLine(lines.get(lineNumber));
            if (values.size() != headers.size()) {
                throw new IllegalArgumentException(
                    "CSV column count mismatch at " + csvPath + ":" + (lineNumber + 1));
            }

            Map<String, String> row = new LinkedHashMap<>();
            for (int column = 0; column < headers.size(); column++) {
                row.put(headers.get(column).trim(), values.get(column).trim());
            }
            rows.add(row);
        }
        return rows;
    }

    private List<String> parseLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    value.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                values.add(value.toString());
                value.setLength(0);
            } else {
                value.append(character);
            }
        }

        if (quoted) {
            throw new IllegalArgumentException("unclosed quoted CSV value");
        }
        values.add(value.toString());
        return values;
    }
}
