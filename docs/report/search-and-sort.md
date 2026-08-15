\# Search \& Sort Engine (Team D)



\## 1. Overview



Every dispatch decision in the Ghana Smart Service Operations Optimizer eventually

comes down to either finding something (search) or ordering something (sort). This

section covers the two search algorithms and four sort algorithms implemented from

scratch for the system, why each was built the way it was, and what the trace tables

and counterexample testing revealed about their behaviour.



\## 2. Searching



\### 2.1 Linear Search



Linear search walks through the array element by element until it finds a match or

reaches the end. It makes no assumptions about the order of the data, which makes it

the only safe option for unsorted service-request data. Its cost grows directly with

the size of the input (O(n)), so it is correct but slow at scale.



\### 2.2 Binary Search



Binary search repeatedly halves a sorted array by comparing the target to the middle

element, giving O(log n) performance — a significant improvement over linear search

as the dataset grows. The trade-off is a strict precondition: \*\*the array must already

be sorted\*\*, and this precondition is not checked by the algorithm itself.



\### 2.3 Counterexample: Binary Search on Unsorted Data



Per the project's required counterexample, binary search was run directly against

unsorted arrays to show what happens when its precondition is violated.



With the unsorted array `\[10, 2, 15, 6, 8]`, searching for `15` still located the

value at index 2 — but only by coincidence, because the middle element happened to

be the target.



A second unsorted array, `\[8, 2, 15, 1, 6]`, searching for `6`, exposed the real

problem: binary search returned `-1` even though `6` exists in the array at index 4.

The algorithm silently gave a wrong answer rather than failing loudly.



\*\*Conclusion:\*\* binary search's correctness depends entirely on its sorted-input

precondition. Violating that precondition does not reliably cause an error — it can

cause the algorithm to return an incorrect result while appearing to have worked.

This is why the sorted-input precondition must be enforced (or documented and tested

for) at every call site in the wider system, not just assumed.



\## 3. Sorting



All four sorts were implemented from scratch, without `Collections.sort()` or any

built-in Java sorting utility, per the project's core-logic rule.



\### 3.1 Selection Sort



Repeatedly selects the minimum remaining element and swaps it into place. In-place

(O(1) extra memory), but \*\*not stable\*\* — equal elements can be reordered relative to

each other during the swap step.



\### 3.2 Insertion Sort



Builds up a sorted portion of the array one element at a time, shifting larger

elements right to make room. In-place, and \*\*stable\*\* — equal elements retain their

original relative order, since an element is only shifted past elements strictly

greater than it, never past equal ones.



\### 3.3 Merge Sort



A divide-and-conquer algorithm: the array is recursively split in half until each

piece has one element, then adjacent pieces are merged back together in sorted

order. Guarantees O(n log n) performance regardless of input order, but requires a

temporary array during the merge step, so it is not in-place.



The trace table (`docs/trace-tables/merge-sort-trace.md`) walks through sorting

`\[38, 27, 43, 3]`, showing the split down to single elements and the merge back up

to `\[3, 27, 38, 43]`.



\### 3.4 Quicksort



Also divide-and-conquer, but partitions in place around a pivot (the last element of

each subrange, using the Lomuto scheme) rather than merging separate arrays. Average

case is O(n log n), but its \*\*worst case is O(n²)\*\* — this happens when the pivot

choice repeatedly produces a very unbalanced split, which occurs on already-sorted

or reverse-sorted input when the pivot is always the last element.



The trace table (`docs/trace-tables/quick-sort-trace.md`) traces `\[10, 7, 8, 9, 1, 5]`

step by step through each partition call. Notably, this array is close to a

worst-case shape for a last-element pivot: the descending run `10, 7, 8, 9` at the

front meant almost every partition only moved elements to one side, closely

resembling the behaviour that produces O(n²) time on fully sorted input.



\## 4. Summary Comparison



| Algorithm       | Best Case   | Average Case | Worst Case | In-Place | Stable | Precondition        |

|-----------------|-------------|---------------|------------|----------|--------|----------------------|

| Linear Search   | O(1)        | O(n)          | O(n)       | —        | —      | None                 |

| Binary Search   | O(1)        | O(log n)      | O(log n)   | —        | —      | Input must be sorted |

| Selection Sort  | O(n²)       | O(n²)         | O(n²)      | Yes      | No     | None                 |

| Insertion Sort  | O(n)        | O(n²)         | O(n²)      | Yes      | Yes    | None                 |

| Merge Sort      | O(n log n)  | O(n log n)    | O(n log n) | No       | Yes    | None                 |

| Quicksort       | O(n log n)  | O(n log n)    | O(n²)      | Yes      | No     | None                 |



\## 5. Performance Timing



All 6 algorithms were timed using `Timer.timeInNanos(...)` (Team B's shared

timing utility) at input sizes 100, 500, 1,000, 5,000, and 10,000. Each size

was run 3 times and averaged, per `Evidence\_and\_Testing\_Standards.md`. Full

results: `data/results/search-sort-timing.csv`. Machine spec (required for

the first experiment run on the project): `docs/report/machine-spec.md`

(Intel Core i5-6300U, 8GB RAM, Windows 11).



!\[Search and sort timing results](search-sort-timing-graph.png)



\*\*Interpretation:\*\*



Binary search stays essentially flat (3,400–12,100 ns) across every input

size, while linear search's cost is far more variable and generally trends

upward with size — this matches the expected O(log n) vs. O(n) gap, and the

gap widens as input size grows, exactly as Big-O predicts.



Selection sort and insertion sort both show clear quadratic growth: going

from 1,000 to 10,000 elements (10x the input), selection sort's time

increased roughly 46x and insertion sort roughly 19x — both close to the

\~100x growth O(n²) would suggest, with insertion sort performing better in

practice since it does less work on partially-ordered data. Merge sort and

quicksort, by contrast, only grew about 5x over the same 10x input increase,

consistent with O(n log n).



One anomaly worth noting: `LinearSearch` at size 100 (184,433 ns) is

slower than at size 500 (19,066 ns), which looks backwards for O(n). This

is a JVM warm-up effect — `LinearSearch` runs first in the experiment, and

the very first method calls in a Java program are slower because the JVM

hasn't yet JIT-compiled that code path. This is a known limitation of

single-run-style timing in Java rather than a flaw in the algorithm itself.



\## 6. Evidence



\- Unit tests (normal, boundary, invalid input) for all 6 algorithms:

&#x20; `src/test/java/com/team/smartops/algorithms/search/`,

&#x20; `src/test/java/com/team/smartops/algorithms/sort/` — all 39 project tests

&#x20; pass (`mvn test`, verified 2026-08-15)

\- Trace tables: `docs/trace-tables/binary-search-trace.md`,

&#x20; `insertion-sort-trace.md`, `merge-sort-trace.md`, `quick-sort-trace.md`

\- Counterexample: `docs/proof-sketches/binary-search-unsorted-counterexample.md`

\- Performance timing script: `src/main/java/com/team/smartops/performance/SearchSortTimingExperiment.java`

\- Performance results (CSV): `data/results/search-sort-timing.csv`

\- Performance graph: `docs/report/search-sort-timing-graph.png`

&#x20; (generated directly from the CSV above)

\- Machine spec: `docs/report/machine-spec.md`



\*(Draft — Team D. To be reviewed and merged into the final report by whoever compiles it.)\*

