# Ghana Smart Service Operations Optimizer — System Architecture

## 1. High-Level Architecture Overview

```mermaid
graph TD
    subgraph UI ["User Interface (Lead)"]
        CLI[App.java & ConsoleMenu.java]
    end

    subgraph DataAccess ["Database Layer (Team B)"]
        DB[(SQLite: smartops.db)]
        DBC[DatabaseConnection]
        DL[DataLoader]
        LR[LocationRepository]
        RR[RoadRepository]
        SRR[ServiceRequestRepository]
        ResR[ResourceRepository]
        ARR[AlgorithmRunRepository]
    end

    subgraph Models ["Domain Models (Shared)"]
        Loc[Location]
        Rd[Road]
        SR[ServiceRequest]
        Res[Resource]
        AR[AlgorithmRun]
        Adapters[ModelAdapters]
    end

    subgraph CoreDS ["Hand-Built Data Structures (Team C)"]
        DA[DynamicArray]
        LL[MyLinkedList]
        Stk[MyStack]
        Q[MyQueue]
        Heap[MyPriority_Heap]
        HT[HashTable / MyMap / MySet]
        BST[BST]
        RBT[RedBlackTree]
        DSU[DisjointSet]
        G[Graph]
    end

    subgraph Algorithms ["Algorithms & Optimisation (Teams D, E, F)"]
        SS[Search & Sort: QuickSort, MergeSort, BinarySearch]
        Routing[Graph Routing: Dijkstra, Prim, Kruskal, BFS, DFS]
        Opt[Optimisation: GreedyAssignment, KnapsackDP, GreedyBudgetSelector]
        GDL[GraphDistanceLookup]
    end

    subgraph Perf ["Performance & Benchmarking (Team G)"]
        TMR[Timer]
        SSTE[SearchSortTimingExperiment]
        OTE[OptimisationTimingExperiment]
    end

    CLI --> DL
    CLI --> Routing
    CLI --> Opt
    CLI --> SS
    CLI --> Perf

    DL --> DBC
    DBC --> DB
    DL --> LR & RR & SRR & ResR
    LR & RR & SRR & ResR --> Loc & Rd & SR & Res

    DL --> G
    G --> LL & Q & DA

    Routing --> G
    Routing --> Heap
    Routing --> DSU

    Opt --> Adapters
    Adapters --> Loc & SR & Res
    Opt --> GDL
    GDL --> Routing

    Perf --> TMR
    Perf --> SS & Opt
    Perf --> ARR
```

---

## 2. Component Responsibilities

| Subsystem | Key Components | Responsibilities |
|---|---|---|
| **User Interface** | `App.java`, `ConsoleMenu.java` | Interactive command-line menu, workflow routing, formatted output presentation. |
| **Database & Models** | `schema.sql`, `DatabaseConnection`, Repositories, Domain Models | Persistence in SQLite, relational entity hydration, typed models. |
| **Data Structures** | `MyPriority_Heap`, `HashTable`, `Graph`, `DynamicArray`, `DisjointSet` | Pure from-scratch data structure implementations satisfying academic constraints. |
| **Graph Routing** | `Dijkstra.java`, `Prim.java`, `Kruskal.java`, `BFS.java`, `DFS.java` | Single-source shortest path, minimum spanning trees, reachability analysis. |
| **Optimisation** | `GreedyAssignment`, `KnapsackDP`, `GreedyBudgetSelector` | Request dispatching, 0/1 knapsack budget allocation, algorithm failure counterexamples. |
| **Performance** | `Timer`, `SearchSortTimingExperiment`, `OptimisationTimingExperiment` | Nanosecond precision timing benchmarks, CSV export, Big-O empirical verification. |
