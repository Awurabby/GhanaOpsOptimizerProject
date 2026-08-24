MERGE SORT TRACE TABLE

This is the trace table for the merge sort algorithm using an array from the test units.

Using the array (38, 27, 43, 3), sorting in ascending order we have

| Step        | Operation         | Array              |      |
| ----------- | ----------------- |--------------------| ---- |
| Initial     | Start             | 38 27 43 3         |      |
| Split       | Left / Right      | 38 27              | 43 3 |
| Split       | Left              | 38                 | 27   |
| Merge       | Merge left        | 27 38              |      |
| Split       | Right             | 43                 | 3    |
| Merge       | Merge right       | 3 43               |      |
| Final Merge | Merge both halves | 3 27 38 43         |      |
