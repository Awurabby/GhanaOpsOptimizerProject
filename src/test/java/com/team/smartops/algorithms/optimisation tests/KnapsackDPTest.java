package com.team.smartops.algorithms.optimisation;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class KnapsackDPTest {

    @Test
    void normalCase_picksBestValueSubsetWithinBudget() {
        List<ServiceRequest> requests = List.of(
            new ServiceRequest("R1", "Z1", "Z2", "cat", 5, 2, 3),
            new ServiceRequest("R2", "Z1", "Z2", "cat", 5, 3, 4),
            new ServiceRequest("R3", "Z1", "Z2", "cat", 5, 4, 5),
            new ServiceRequest("R4", "Z1", "Z2", "cat", 5, 5, 6)
        );
        int budget = 5;

        KnapsackDP.Result result = new KnapsackDP().solveWithReconstruction(requests, budget);

        // Best combo within budget 5: R1+R2 (cost 5, value 7) beats R4 alone (cost 5, value 6)
        assertEquals(7, result.bestValue);
        assertEquals(2, result.selected.size());
    }

    @Test
    void boundaryCase_zeroBudget_selectsNothing() {
        List<ServiceRequest> requests = List.of(
            new ServiceRequest("R1", "Z1", "Z2", "cat", 5, 2, 3)
        );

        KnapsackDP.Result result = new KnapsackDP().solveWithReconstruction(requests, 0);

        assertEquals(0, result.bestValue);
        assertTrue(result.selected.isEmpty());
    }

    @Test
    void boundaryCase_singleRequestExactlyFitsBudget() {
        List<ServiceRequest> requests = List.of(
            new ServiceRequest("R1", "Z1", "Z2", "cat", 5, 4, 10)
        );

        KnapsackDP.Result result = new KnapsackDP().solveWithReconstruction(requests, 4);

        assertEquals(10, result.bestValue);
        assertEquals(List.of("R1"), result.selected.stream().map(ServiceRequest::getRequestId).toList());
    }

    @Test
    void invalidInput_emptyRequestList_returnsZeroValue() {
        KnapsackDP.Result result = new KnapsackDP().solveWithReconstruction(Collections.emptyList(), 10);

        assertEquals(0, result.bestValue);
        assertTrue(result.selected.isEmpty());
    }

    @Test
    void reconstruction_selectedRequestsCostSumsWithinBudget() {
        List<ServiceRequest> requests = List.of(
            new ServiceRequest("R1", "Z1", "Z2", "cat", 5, 2, 3),
            new ServiceRequest("R2", "Z1", "Z2", "cat", 5, 3, 4),
            new ServiceRequest("R3", "Z1", "Z2", "cat", 5, 4, 5)
        );
        int budget = 6;

        KnapsackDP.Result result = new KnapsackDP().solveWithReconstruction(requests, budget);

        int totalCost = result.selected.stream().mapToInt(ServiceRequest::getCost).sum();
        assertTrue(totalCost <= budget, "reconstructed selection must respect the budget");

        int totalValue = result.selected.stream().mapToInt(ServiceRequest::getValue).sum();
        assertEquals(result.bestValue, totalValue, "reconstructed selection's value must match dp[n][budget]");
    }
}