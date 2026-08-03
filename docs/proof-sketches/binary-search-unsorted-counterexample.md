# Binary Search on an Unsorted Array Counterexample

## Objective
To demonstrate that binary search only works correctly on a sorted array.

## Input Array (Unsorted)
[10, 2, 15, 6, 8]
Target:15

## Expected Result
Since the value 15 exists in the array, a correct search algorithm should locate it.

## Binary Search Execution
Initial values:
Low = 0
High = 4
Mid = 2
Value at Mid = 15
Depending on the arrangement of an unsorted array, binary search may sometimes appear to work by coincidence. However, this behavior is not reliable.
For example, with another unsorted array:[8, 2, 15, 1, 6]

Searching for:6. 
Binary search returns -1 even though the value exists.

## Conclusion
Binary search assumes that the array is sorted before searching begins.
When this precondition is violated, the algorithm may return incorrect results or fail to find existing values.
Therefore, binary search should only be used on sorted data.