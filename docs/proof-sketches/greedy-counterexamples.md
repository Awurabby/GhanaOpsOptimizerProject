# Greedy Counterexamples

## Nearest-Resource Assignment

Two requests are processed in urgency order: Balme Library first, then University Hospital.
Two resources are available: Van-A and Van-B.

| Distance | Van-A | Van-B |
|---|---:|---:|
| Balme Library | 1 | 2 |
| University Hospital | 2 | 100 |

Greedy assigns the first request to its nearest resource, Van-A, at distance 1. Only Van-B then
remains for University Hospital, at distance 100. Total distance is 101.

A better assignment sends Van-B to Balme Library at distance 2 and Van-A to University Hospital
at distance 2. Total distance is 4. The greedy choice is locally shortest but makes the complete
assignment 97 distance units worse.

## Value-to-Cost Budget Selection

Budget is 4.

| Request | Cost | Value | Value/cost |
|---|---:|---:|---:|
| A | 3 | 5 | 1.67 |
| B | 2 | 3 | 1.50 |
| C | 2 | 3 | 1.50 |

Ratio greedy selects A first. Only one budget unit remains, so neither B nor C fits; total value
is 5. Knapsack DP selects B and C at total cost 4 and total value 6. This proves that ratio greedy
does not always produce the optimal 0/1 selection.
