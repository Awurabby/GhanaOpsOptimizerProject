# Knapsack DP Correctness Proof Sketch

## Claim

For every `i` from 0 to the number of requests and every budget `b`, `dp[i][b]` stores the
maximum value obtainable by selecting from the first `i` requests without spending more than
`b`.

## Base Case

With zero requests, no value can be selected, so `dp[0][b] = 0` for every budget. With zero
budget, only zero-cost requests could be selected; the project model rejects non-positive costs,
so `dp[i][0] = 0`.

## Inductive Step

Assume the claim is true for the first `i - 1` requests. Consider request `i`.

- If its cost is greater than `b`, it cannot belong to any valid solution. The best value is
  therefore `dp[i-1][b]`.
- Otherwise, every valid solution either excludes request `i` or includes it. The best excluding
  value is `dp[i-1][b]`. If included, the remaining requests must fit in `b - cost[i]`; by the
  induction assumption their best value is `dp[i-1][b-cost[i]]`. Adding request `i` gives
  `value[i] + dp[i-1][b-cost[i]]`.

Taking the larger of those two values covers every valid solution and stores an achievable
value. Therefore the claim also holds for `i`. By induction, `dp[n][B]` is optimal.

## Reconstruction

When `dp[i][b]` differs from `dp[i-1][b]`, request `i` was needed to obtain that value, so the
algorithm records it and subtracts its cost. Otherwise it skips the request. Repeating this step
reaches the base row and produces a valid set whose value equals `dp[n][B]`.
