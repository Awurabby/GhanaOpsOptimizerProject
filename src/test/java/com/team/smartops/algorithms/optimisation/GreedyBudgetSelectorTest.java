package com.team.smartops.algorithms.optimisation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GreedyBudgetSelectorTest {

    @Test
    void counterexample_dpFindsBetterValueOnTheSameBudgetProblem() {
        List<ServiceRequest> requests = List.of(
            request("A", 3, 5),
            request("B", 2, 3),
            request("C", 2, 3)
        );
        int budget = 4;

        GreedyBudgetSelector.Result greedy =
            new GreedyBudgetSelector().select(requests, budget);
        KnapsackDP.Result dp =
            new KnapsackDP().solveWithReconstruction(requests, budget);

        assertEquals(List.of("A"), requestIds(greedy.selected));
        assertEquals(3, greedy.totalCost);
        assertEquals(5, greedy.totalValue);
        assertEquals(List.of("B", "C"), requestIds(dp.selected));
        assertEquals(6, dp.bestValue);
        assertTrue(dp.bestValue > greedy.totalValue);
    }

    @Test
    void invalidInput_nullRequestList_isRejected() {
        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> new GreedyBudgetSelector().select(null, 4)
        );

        assertEquals("requests must not be null", error.getMessage());
    }

    @Test
    void invalidInput_negativeBudget_isRejected() {
        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> new GreedyBudgetSelector().select(List.of(), -1)
        );

        assertEquals("budget must not be negative", error.getMessage());
    }

    @Test
    void invalidInput_negativeRequestCost_isRejected() {
        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> new GreedyBudgetSelector().select(List.of(request("A", -1, 5)), 4)
        );

        assertEquals("request cost must not be negative: A", error.getMessage());
    }

    @Test
    void invalidInput_negativeRequestValue_isRejected() {
        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> new GreedyBudgetSelector().select(List.of(request("A", 1, -5)), 4)
        );

        assertEquals("request value must not be negative: A", error.getMessage());
    }

    @Test
    void boundaryCase_zeroBudget_selectsNothing() {
        GreedyBudgetSelector.Result result =
            new GreedyBudgetSelector().select(List.of(request("A", 1, 5)), 0);

        assertTrue(result.selected.isEmpty());
        assertEquals(0, result.totalCost);
        assertEquals(0, result.totalValue);
    }

    private ServiceRequest request(String id, int cost, int value) {
        return new ServiceRequest(id, "Balme Library", "Pentagon Hostel",
            "Maintenance", value, cost, value);
    }

    private List<String> requestIds(List<ServiceRequest> requests) {
        return requests.stream().map(ServiceRequest::getRequestId).toList();
    }
}
