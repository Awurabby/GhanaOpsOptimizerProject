QUICK SORT TRACE TABLE



This is the trace table for the quicksort algorithm using the array from the test units (testNormalCase).



Using the array \[10, 7, 8, 9, 1, 5], pivot = last element of each subrange (Lomuto partition scheme), sorting in ascending order:



| Call quickSort(low, high) | Pivot | Comparisons (arr\[j] <= pivot) | Array After Partition | Pivot Index |

| -------------------------- | ----: | ------------------------------ | ---------------------- | ----------: |

| (0, 5)                      |     5 | 10>5, 7>5, 8>5, 9>5, 1<=5 (swap 1 into place)   | 1 5 8 9 10 7           |           1 |

| (2, 5) — right of pivot 5    |     7 | 8>7, 9>7, 10>7                 | 1 5 7 9 10 8           |           2 |

| (3, 5) — right of pivot 7    |     8 | 9>8, 10>8                      | 1 5 7 8 10 9           |           3 |

| (4, 5) — right of pivot 8    |     9 | 10>9                           | 1 5 7 8 9 10           |           4 |

| (0, 0), (2, 1), (3, 2), (4, 3), (5, 5) | — | low >= high, base case reached, no further partitioning | — | — |



Final sorted array: 1 5 7 8 9 10



Note on this array: with an already-partially-descending run at the front (10, 7, 8, 9) and the pivot always chosen as the last element, every partition step here only ever moved elements into the right side except for one swap — this is close to quicksort's worst-case behaviour (already-sorted/reverse-sorted input degrading to O(n^2)), which is discussed further in the report alongside the recursion tree for a fully-sorted input.

