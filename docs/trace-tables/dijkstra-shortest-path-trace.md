# DIJKSTRA'S SHORTEST PATH TRACE TABLE

## Overview
This trace table tracks Dijkstra's single-source shortest path algorithm on the core campus subgraph starting from **Node 0 (Balme Library)** using the custom `MyPriority_Heap` min-heap.

### Graph Setup
- **Node 0**: Balme Library
- **Node 1**: Pentagon Hostel
- **Node 2**: University Hospital
- **Node 3**: JQB Building
- **Node 4**: Department of Physics

### Edges (Weights in km)
- (0, 1) = 1.5 km
- (0, 2) = 0.8 km
- (0, 3) = 0.4 km
- (1, 4) = 1.2 km
- (3, 4) = 0.3 km

---

## Step-by-Step Execution Trace

| Step | Extracted Node | Current Dist | Unvisited Neighbors Evaluated | Relaxation / Candidate Dist | Updated Distances `[d0, d1, d2, d3, d4]` | Predecessors `[p0, p1, p2, p3, p4]` | Min-Heap State (Vertex, Dist) |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| **Init** | — | — | — | — | `[0, ∞, ∞, ∞, ∞]` | `[-1, -1, -1, -1, -1]` | `[(0, 0.0)]` |
| **1** | **0 (Balme Library)** | 0.0 | 1, 2, 3 | d(1)=1.5, d(2)=0.8, d(3)=0.4 | `[0, 1.5, 0.8, 0.4, ∞]` | `[-1, 0, 0, 0, -1]` | `[(3, 0.4), (2, 0.8), (1, 1.5)]` |
| **2** | **3 (JQB Building)** | 0.4 | 0 (skip), 4 | 0.4 + 0.3 = 0.7 < ∞ (d(4)=0.7) | `[0, 1.5, 0.8, 0.4, 0.7]` | `[-1, 0, 0, 0, 3]` | `[(4, 0.7), (2, 0.8), (1, 1.5)]` |
| **3** | **4 (Dept of Physics)** | 0.7 | 1, 3 (skip) | 0.7 + 1.2 = 1.9 > 1.5 (no change) | `[0, 1.5, 0.8, 0.4, 0.7]` | `[-1, 0, 0, 0, 3]` | `[(2, 0.8), (1, 1.5)]` |
| **4** | **2 (Univ Hospital)** | 0.8 | 0 (skip) | No unvisited neighbors | `[0, 1.5, 0.8, 0.4, 0.7]` | `[-1, 0, 0, 0, 3]` | `[(1, 1.5)]` |
| **5** | **1 (Pentagon Hostel)** | 1.5 | 0 (skip), 4 (skip) | No unvisited neighbors | `[0, 1.5, 0.8, 0.4, 0.7]` | `[-1, 0, 0, 0, 3]` | `[]` (Empty) |

---

## Final Results from Source Node 0 (Balme Library)

| Destination Vertex | Target Location Name | Shortest Distance | Predecessor Chain (Path) |
|:---:|:---|:---:|:---|
| **0** | Balme Library | 0.0 km | `[0]` |
| **1** | Pentagon Hostel | 1.5 km | `[0 -> 1]` |
| **2** | University Hospital | 0.8 km | `[0 -> 2]` |
| **3** | JQB Building | 0.4 km | `[0 -> 3]` |
| **4** | Department of Physics | 0.7 km | `[0 -> 3 -> 4]` |

### Complexity Analysis:
- Priority Queue insertions and extractions: $O((V + E) \log V)$ using binary min-heap `MyPriority_Heap`.
- Space Complexity: $O(V)$ for distance and predecessor tables.
