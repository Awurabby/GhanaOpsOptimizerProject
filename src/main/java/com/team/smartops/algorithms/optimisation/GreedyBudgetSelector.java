package com.team.smartops.algorithms.optimisation;

import java.util.ArrayList;
import java.util.List;

/**
 * Greedy baseline for the same budget-selection problem solved by KnapsackDP.
 * Requests are considered from highest to lowest value-to-cost ratio and are
 * selected when they fit within the remaining budget.
 */
public class GreedyBudgetSelector {

    public static class Result {
        public final List<ServiceRequest> selected;
        public final int totalCost;
        public final int totalValue;

        Result(List<ServiceRequest> selected, int totalCost, int totalValue) {
            this.selected = selected;
            this.totalCost = totalCost;
            this.totalValue = totalValue;
        }
    }

    public Result select(List<ServiceRequest> requests, int budget) {
        if (requests == null) {
            throw new IllegalArgumentException("requests must not be null");
        }
        if (budget < 0) {
            throw new IllegalArgumentException("budget must not be negative");
        }
        for (ServiceRequest request : requests) {
            if (request.getCost() < 0) {
                throw new IllegalArgumentException(
                    "request cost must not be negative: " + request.getRequestId());
            }
            if (request.getValue() < 0) {
                throw new IllegalArgumentException(
                    "request value must not be negative: " + request.getRequestId());
            }
        }

        List<ServiceRequest> ordered = orderByValueToCost(requests);
        List<ServiceRequest> selected = new ArrayList<>();
        int totalCost = 0;
        int totalValue = 0;

        for (ServiceRequest request : ordered) {
            if (totalCost + request.getCost() <= budget) {
                selected.add(request);
                totalCost += request.getCost();
                totalValue += request.getValue();
            }
        }

        return new Result(selected, totalCost, totalValue);
    }

    private List<ServiceRequest> orderByValueToCost(List<ServiceRequest> requests) {
        List<ServiceRequest> ordered = new ArrayList<>(requests);

        for (int i = 1; i < ordered.size(); i++) {
            ServiceRequest current = ordered.get(i);
            int j = i - 1;

            while (j >= 0 && comesBefore(current, ordered.get(j))) {
                ordered.set(j + 1, ordered.get(j));
                j--;
            }
            ordered.set(j + 1, current);
        }

        return ordered;
    }

    private boolean comesBefore(ServiceRequest first, ServiceRequest second) {
        long firstRatio = (long) first.getValue() * second.getCost();
        long secondRatio = (long) second.getValue() * first.getCost();

        if (firstRatio != secondRatio) {
            return firstRatio > secondRatio;
        }
        return first.getRequestId().compareTo(second.getRequestId()) < 0;
    }
}
