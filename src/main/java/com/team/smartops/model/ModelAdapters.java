package com.team.smartops.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Converts DB domain models into the algorithm-specific models used by the
 * optimisation package.
 *
 * This adapter exists because the optimisation algorithms use string-based
 * location names and explicit cost/value fields, while the database stores
 * integer foreign keys and derives cost/value from category and urgency.
 * Keeping both representations and bridging them here avoids breaking the
 * 26+ existing optimisation test call sites.
 */
public final class ModelAdapters {

    private ModelAdapters() {} // utility class

    /**
     * Converts a DB service request into an optimisation algorithm input.
     *
     * @param dbRequest      the database-mapped service request
     * @param locationNames  map from locationId to location name
     * @return an optimisation ServiceRequest with resolved location names
     *         and computed cost/value
     * @throws IllegalArgumentException if source or destination location ID
     *         cannot be resolved
     */
    public static com.team.smartops.algorithms.optimisation.ServiceRequest
            toOptServiceRequest(ServiceRequest dbRequest,
                                Map<Integer, String> locationNames) {

        String sourceName = locationNames.get(dbRequest.getSource());
        if (sourceName == null) {
            throw new IllegalArgumentException(
                    "unknown source location ID: " + dbRequest.getSource());
        }

        String destName = locationNames.get(dbRequest.getDestination());
        if (destName == null) {
            throw new IllegalArgumentException(
                    "unknown destination location ID: " + dbRequest.getDestination());
        }

        return new com.team.smartops.algorithms.optimisation.ServiceRequest(
                String.valueOf(dbRequest.getRequestId()),
                sourceName,
                destName,
                dbRequest.getCategory(),
                dbRequest.getUrgencyScore(),
                dbRequest.getEstimatedCost(),
                dbRequest.getValue());
    }

    /**
     * Batch converts a list of DB service requests, keeping only pending ones.
     */
    public static List<com.team.smartops.algorithms.optimisation.ServiceRequest>
            toOptPendingRequests(List<ServiceRequest> dbRequests,
                                Map<Integer, String> locationNames) {

        List<com.team.smartops.algorithms.optimisation.ServiceRequest> result =
                new ArrayList<>();

        for (ServiceRequest dbReq : dbRequests) {
            if (dbReq.isPending()) {
                result.add(toOptServiceRequest(dbReq, locationNames));
            }
        }
        return result;
    }

    /**
     * Converts a DB resource into an optimisation algorithm input.
     *
     * @param dbResource     the database-mapped resource
     * @param locationNames  map from locationId to location name
     * @return an optimisation Resource with the location name resolved
     */
    public static com.team.smartops.algorithms.optimisation.Resource
            toOptResource(Resource dbResource,
                          Map<Integer, String> locationNames) {

        String homeName = locationNames.get(dbResource.getHomeLocation());
        if (homeName == null) {
            homeName = "Location-" + dbResource.getHomeLocation();
        }

        return new com.team.smartops.algorithms.optimisation.Resource(
                String.valueOf(dbResource.getResourceId()),
                dbResource.getType(),
                homeName,
                dbResource.isAvailable());
    }

    /**
     * Batch converts a list of DB resources.
     */
    public static List<com.team.smartops.algorithms.optimisation.Resource>
            toOptResources(List<Resource> dbResources,
                           Map<Integer, String> locationNames) {

        List<com.team.smartops.algorithms.optimisation.Resource> result =
                new ArrayList<>();

        for (Resource dbRes : dbResources) {
            result.add(toOptResource(dbRes, locationNames));
        }
        return result;
    }

    /**
     * Builds a locationId → name lookup map from a list of locations.
     */
    public static Map<Integer, String> buildLocationNameIndex(
            List<Location> locations) {

        // Using java.util.HashMap here — this is infrastructure code,
        // not assessed core logic, per the project rules.
        Map<Integer, String> index = new java.util.LinkedHashMap<>();
        for (Location loc : locations) {
            index.put(loc.getLocationId(), loc.getName());
        }
        return index;
    }
}
