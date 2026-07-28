# Ghana Smart Service Operations Optimizer
DCIT 204/308 Joint DSA Project — Team of 15

## What this is
A console-based Java system that loads Ghana-context service data from a
database into hand-built data structures, runs search/sort/graph/optimisation
algorithms on it, and measures performance as data grows. Full requirements
are in the project brief (kept out of this repo -- see your team's shared drive).

## Getting set up (everyone does this once)

1. Install **Java 17+** and **Maven**.
   Check with: `java -version` and `mvn -version`
2. Clone the repo:
   ```
   git clone <repo-url>
   cd ghana-smart-ops
   ```
3. Build it:
   ```
   mvn compile
   ```
4. Run the console menu:
   ```
   mvn exec:java -Dexec.mainClass="com.team.smartops.App"
   ```
   (or just run `App.java` from your IDE)
5. Run the tests:
   ```
   mvn test
   ```

## Folder map

| Folder | What lives here | Owner |
|---|---|---|
| `src/main/java/.../db/` | Database connection, schema setup, repositories | Team B |
| `src/main/java/.../structures/` | All hand-built data structures | Team C |
| `src/main/java/.../algorithms/search/`, `/sort/` | Search + sort algorithms | Team D |
| `src/main/java/.../algorithms/graph/` | BFS, DFS, Dijkstra, Prim, Kruskal | Team E |
| `src/main/java/.../algorithms/optimisation/` | Greedy + DP | Team F |
| `src/main/java/.../performance/` | Timing + experiment runner | Team G |
| `src/main/java/.../ui/` | Console menu logic | Lead |
| `src/main/resources/schema.sql` | Database schema | Team B |
| `src/main/resources/seed/` | Seed CSVs (Ghana-context data) | Team B |
| `src/test/` | Unit tests -- mirrors the main package structure | everyone, own your own code |
| `data/csv/` | Raw data before DB import | Team B |
| `data/results/` | Exported performance CSVs | Team G |
| `docs/trace-tables/` | Required trace tables (min. 6) | whoever owns that algorithm |
| `docs/proof-sketches/` | Required proof sketches (min. 3) | whoever owns that algorithm |
| `docs/diagrams/` | Structure/architecture diagrams for the report | everyone |
| `docs/report/` | Final report drafts, per section | everyone |

## Branching workflow

- `main` is always working. Never commit directly to it.
- One branch per sub-team: `db`, `core-ds`, `search-sort`, `graph`, `optimisation`, `testing-perf`.
- Open a pull request into `main` when your piece is ready. Get one review before merging.
- Commit messages should reference the brief, e.g.:
  `Implement BST insert/search + inorder traversal (Sec 6)`
- One GitHub Issue per checklist item (see `.github/ISSUE_TEMPLATE/requirement.md`) --
  this becomes your running proof that nothing in the brief was missed.

## Rules for this codebase

- No `java.util.HashMap`, `Stack`, `PriorityQueue`, `ArrayDeque`, `TreeMap`, or
  similar for **assessed core logic** (structures/, most of algorithms/). Built-ins
  are fine for file I/O, JDBC, and test scaffolding.
- Every structure needs tests for: normal case, boundary case, invalid input.
- Don't merge to `main` without at least one trace table or test proving the
  thing works, if the brief requires one for that item.
