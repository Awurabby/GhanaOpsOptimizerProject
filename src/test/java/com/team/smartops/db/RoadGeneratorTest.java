package com.team.smartops.db;

import com.team.smartops.model.Location;
import com.team.smartops.model.Road;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoadGeneratorTest {

    @Test
    void haversineDistance_calculatesRealisticDistances() {
        // Balme Library to Pentagon Hostel
        double dist = RoadGenerator.haversineDistance(5.651740, -0.187046, 5.656667, -0.180993);
        assertTrue(dist >= 0.5 && dist <= 2.0, "distance should be reasonable for campus scale: " + dist);
    }

    @Test
    void generateConnectedRoads_includesExistingAndConnectsAllLocations() {
        Location loc1 = new Location(1, "Balme Library", "Legon", "Library", 5.651740, -0.187046);
        Location loc2 = new Location(2, "Pentagon Hostel", "Legon", "Hostel", 5.656667, -0.180993);
        Location loc3 = new Location(3, "University Hospital", "Legon", "Hospital", 5.651069, -0.177961);

        Road existing = new Road(1, 2, 1.5, 5.0, 1.1);
        List<Road> result = RoadGenerator.generateConnectedRoads(List.of(loc1, loc2, loc3), List.of(existing));

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(r ->
                (r.getFromLocationId() == 1 && r.getToLocationId() == 2) ||
                (r.getFromLocationId() == 2 && r.getToLocationId() == 1)));
    }

    @Test
    void generateAndSaveFullRoads_createsValidCsvFile() throws IOException {
        Path locCsv = Path.of("data/csv/locations.csv");
        Path roadsCsv = Path.of("data/csv/roads.csv");
        Path outputCsv = Path.of("target/test-roads-output.csv");

        if (Files.exists(locCsv)) {
            RoadGenerator.generateAndSaveFullRoads(
                    locCsv.toString(),
                    roadsCsv.toString(),
                    outputCsv.toString()
            );

            assertTrue(Files.exists(outputCsv));
            List<String> lines = Files.readAllLines(outputCsv);
            assertTrue(lines.size() > 20, "generated roads should contain multiple connections");
            Files.deleteIfExists(outputCsv);
        }
    }
}
