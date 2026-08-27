package com.team.smartops.model;

import java.util.Locale;
import java.util.Objects;

/**
 * Domain model for the {@code service_requests} table.
 * Fields match schema.sql: requestId, source, destination, category,
 * urgency, timeSubmitted, deadline, status.
 *
 * Also exposes derived properties required by the optimisation algorithms
 * ({@link #getUrgencyScore()}, {@link #getEstimatedCost()}, {@link #getValue()}).
 * These use the same logic that was previously in
 * {@code OptimisationCsvLoader} — centralised here so every consumer
 * computes them consistently.
 */
public class ServiceRequest {

    private final int requestId;
    private final int source;
    private final int destination;
    private final String category;
    private final String urgency;
    private final String timeSubmitted;
    private final String deadline;
    private final String status;

    public ServiceRequest(int requestId, int source, int destination,
                          String category, String urgency,
                          String timeSubmitted, String deadline,
                          String status) {
        this.requestId = requestId;
        this.source = source;
        this.destination = destination;
        this.category = category;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
    }

    // ---- direct DB field accessors ----

    public int getRequestId()       { return requestId; }
    public int getSource()          { return source; }
    public int getDestination()     { return destination; }
    public String getCategory()     { return category; }
    public String getUrgency()      { return urgency; }
    public String getTimeSubmitted() { return timeSubmitted; }
    public String getDeadline()     { return deadline; }
    public String getStatus()       { return status; }

    // ---- derived properties for optimisation algorithms ----

    /**
     * Converts the string urgency level to a numeric score.
     * Critical = 4, High = 3, Medium = 2, Low = 1.
     *
     * @throws IllegalStateException if the urgency value is unrecognised
     */
    public int getUrgencyScore() {
        if (urgency == null) {
            throw new IllegalStateException("urgency is null for request " + requestId);
        }
        return switch (urgency.toLowerCase(Locale.ROOT)) {
            case "critical" -> 4;
            case "high" -> 3;
            case "medium" -> 2;
            case "low" -> 1;
            default -> throw new IllegalStateException(
                    "unknown urgency for request " + requestId + ": " + urgency);
        };
    }

    /**
     * Estimated cost based on category — represents relative effort,
     * not currency or distance. Policy defined by Team F:
     * electrical=5, maintenance/repair/plumbing/water=4,
     * security/internet/network/equipment=3,
     * cleaning/inspection/complaint=2, other=3.
     */
    public int getEstimatedCost() {
        if (category == null) return 3;
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

    /**
     * Benefit value if this request is served.
     * Follows the formula used in the optimisation experiments:
     * {@code urgencyScore * 10}.
     */
    public int getValue() {
        return getUrgencyScore() * 10;
    }

    public boolean isPending() {
        return "Pending".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return requestId + " - " + category
                + " | Urgency: " + urgency
                + " | Status: " + status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceRequest other)) return false;
        return requestId == other.requestId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }
}
