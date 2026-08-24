package com.team.smartops.algorithms.optimisation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OptimisationCsvLoaderTest {

    @TempDir
    Path tempDirectory;

    @Test
    void loadPendingRequests_mapsDatabaseFieldsIntoOptimisationInputs() throws IOException {
        Path locations = write("locations.csv", """
            locationId,name,area,type,latitude,longitude
            1,Balme Library,Legon,Library,5.65,-0.18
            2,Pentagon Hostel,Legon,Hostel,5.66,-0.18
            """);
        Path requests = write("requests.csv", """
            requestId,source,destination,category,urgency,timeSubmitted,deadline,status
            1,1,2,Hostel Maintenance,High,2026-07-01 08:30,2026-07-01 18:30,Pending
            2,2,1,Cleaning Request,Low,2026-07-01 09:00,2026-07-02 09:00,Resolved
            """);

        List<ServiceRequest> result =
            new OptimisationCsvLoader().loadPendingRequests(requests, locations);

        assertEquals(1, result.size());
        ServiceRequest request = result.get(0);
        assertEquals("1", request.getRequestId());
        assertEquals("Balme Library", request.getSource());
        assertEquals("Pentagon Hostel", request.getDestination());
        assertEquals(3, request.getUrgency());
        assertEquals(4, request.getCost());
        assertEquals(30, request.getValue());
    }

    @Test
    void loadPendingRequests_rejectsUnknownUrgency() throws IOException {
        Path locations = write("locations.csv", """
            locationId,name,area,type,latitude,longitude
            1,Balme Library,Legon,Library,5.65,-0.18
            """);
        Path requests = write("requests.csv", """
            requestId,source,destination,category,urgency,timeSubmitted,deadline,status
            1,1,1,Maintenance,Immediate,2026-07-01 08:30,2026-07-01 18:30,Pending
            """);

        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> new OptimisationCsvLoader().loadPendingRequests(requests, locations)
        );

        assertEquals("unknown urgency for request 1: Immediate", error.getMessage());
    }

    private Path write(String fileName, String content) throws IOException {
        Path path = tempDirectory.resolve(fileName);
        Files.writeString(path, content);
        return path;
    }
}
