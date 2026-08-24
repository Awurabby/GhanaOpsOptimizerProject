# Knapsack Dynamic Programming Trace

## Example

Budget `B = 6`. Each request can be selected at most once.

| Request | Cost | Value |
|---|---:|---:|
| R1 | 2 | 3 |
| R2 | 3 | 4 |
| R3 | 4 | 5 |
| R4 | 5 | 8 |
| R5 | 1 | 2 |

`dp[i][b]` is the best value obtainable from the first `i` requests with budget `b`.

```text
dp[i][b] = dp[i-1][b]                                      when cost[i] > b
dp[i][b] = max(dp[i-1][b], value[i] + dp[i-1][b-cost[i]]) otherwise
```

## Completed Table

| Requests considered | b=0 | b=1 | b=2 | b=3 | b=4 | b=5 | b=6 |
|---|---:|---:|---:|---:|---:|---:|---:|
| None | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| R1 | 0 | 0 | 3 | 3 | 3 | 3 | 3 |
| R1-R2 | 0 | 0 | 3 | 4 | 4 | 7 | 7 |
| R1-R3 | 0 | 0 | 3 | 4 | 5 | 7 | 8 |
| R1-R4 | 0 | 0 | 3 | 4 | 5 | 8 | 8 |
| R1-R5 | 0 | 2 | 3 | 5 | 6 | 8 | **10** |

For example, at `dp[5][6]`, excluding R5 gives value 8. Including R5 gives
`2 + dp[4][5] = 10`, so the algorithm stores 10.

## Reconstruction

1. Start at `dp[5][6] = 10`. It differs from `dp[4][6] = 8`, so select R5 and move to budget 5.
2. `dp[4][5] = 8` differs from `dp[3][5] = 7`, so select R4 and move to budget 0.
3. Stop because no budget remains.

The selected requests are R4 and R5. Their total cost is `5 + 1 = 6`, and their total value is
`8 + 2 = 10`, matching the bottom-right table entry.
