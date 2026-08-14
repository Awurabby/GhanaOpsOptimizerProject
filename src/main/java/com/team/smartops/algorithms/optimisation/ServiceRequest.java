package com.team.smartops.algorithms.optimisation;

/**
 * PLACEHOLDER model.
 *
 * Will swap this out for the real ServiceRequest class once the Database team
 * (or the shared model package) publishes theirs — field names should match
 * the service_requests table: requestId, source, destination, category,
 * urgency, timeSubmitted, deadline, status.
 *
 * Two fields have been added here (cost, value) purely for the knapsack DP —
 * confirm with the team whether these belong on the shared model or should
 * stay local to the optimisation package.
 */
public class ServiceRequest {

    private final String requestId;
    private final String source;
    private final String destination;
    private final String category;
    private final int urgency;      // higher = more urgent
    private final int cost;         // resource/budget units required (for DP)
    private final int value;        // benefit if served (for DP) -- often == urgency

    public ServiceRequest(String requestId, String source, String destination,
                           String category, int urgency, int cost, int value) {
        this.requestId = requestId;
        this.source = source;
        this.destination = destination;
        this.category = category;
        this.urgency = urgency;
        this.cost = cost;
        this.value = value;
    }

    public String getRequestId() { return requestId; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public String getCategory() { return category; }
    public int getUrgency() { return urgency; }
    public int getCost() { return cost; }
    public int getValue() { return value; }

    @Override
    public String toString() {
        return requestId + "(urgency=" + urgency + ", cost=" + cost + ", value=" + value + ")";
    }
}