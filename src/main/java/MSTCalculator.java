import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Provides implementations of Prim's and Kruskal's algorithms for MST computation.
 */
public class MSTCalculator {

    public MSTResult computePrim(Graph graph) {
        long start = System.nanoTime();
        OperationCounter counter = new OperationCounter();

        List<Edge> mstEdges = new ArrayList<>();
        double totalCost = 0.0;

        List<String> nodes = graph.getNodes();
        if (nodes.isEmpty()) {
            long end = System.nanoTime();
            return new MSTResult(mstEdges, totalCost, counter.getComparisons(), counter.getUpdates(), counter.getUnions(),
                    toMillis(start, end), true);
        }

        Map<String, List<Edge>> adjacency = buildAdjacency(graph);
        Set<String> visited = new HashSet<>();
        String startNode = nodes.getFirst();
        visited.add(startNode);
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        for (Edge edge : adjacency.getOrDefault(startNode, List.of())) {
            queue.offer(edge);
            counter.incrementUpdate();
        }

        while (!queue.isEmpty() && mstEdges.size() < nodes.size() - 1) {
            Edge smallest = queue.poll();
            counter.incrementUpdate();
            String next = visited.contains(smallest.getFrom()) ? smallest.getTo() : smallest.getFrom();
            counter.incrementComparison();
            if (visited.contains(next)) {
                counter.incrementComparison();
                continue;
            }
            visited.add(next);
            mstEdges.add(smallest);
            totalCost += smallest.getWeight();

            for (Edge edge : adjacency.getOrDefault(next, List.of())) {
                String opposite = edge.getFrom().equals(next) ? edge.getTo() : edge.getFrom();
                counter.incrementComparison();
                if (!visited.contains(opposite)) {
                    queue.offer(edge);
                    counter.incrementUpdate();
                }
            }
        }

        boolean connected = visited.size() == nodes.size();
        long end = System.nanoTime();
        return new MSTResult(mstEdges, totalCost, counter.getComparisons(), counter.getUpdates(), counter.getUnions(),
                toMillis(start, end), connected);
    }

    public MSTResult computeKruskal(Graph graph) {
        long start = System.nanoTime();
        OperationCounter counter = new OperationCounter();

        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());
        sortedEdges.sort((a, b) -> {
            counter.incrementComparison();
            return Double.compare(a.getWeight(), b.getWeight());
        });

        UnionFind unionFind = new UnionFind(graph.getNodes());
        List<Edge> mstEdges = new ArrayList<>();
        double totalCost = 0.0;

        for (Edge edge : sortedEdges) {
            String u = edge.getFrom();
            String v = edge.getTo();
            String rootU = unionFind.find(u);
            counter.incrementUpdate();
            String rootV = unionFind.find(v);
            counter.incrementUpdate();
            if (!rootU.equals(rootV)) {
                counter.incrementComparison();
                unionFind.union(rootU, rootV);
                counter.incrementUnion();
                mstEdges.add(edge);
                totalCost += edge.getWeight();
            } else {
                counter.incrementComparison();
            }
            if (mstEdges.size() == graph.vertexCount() - 1) {
                break;
            }
        }

        boolean connected = mstEdges.size() == Math.max(0, graph.vertexCount() - 1);
        long end = System.nanoTime();
        return new MSTResult(mstEdges, totalCost, counter.getComparisons(), counter.getUpdates(), counter.getUnions(),
                toMillis(start, end), connected);
    }

    private Map<String, List<Edge>> buildAdjacency(Graph graph) {
        Map<String, List<Edge>> adjacency = new HashMap<>();
        for (Edge edge : graph.getEdges()) {
            adjacency.computeIfAbsent(edge.getFrom(), key -> new ArrayList<>()).add(edge);
            adjacency.computeIfAbsent(edge.getTo(), key -> new ArrayList<>()).add(edge);
        }
        return adjacency;
    }

    private double toMillis(long start, long end) {
        return (end - start) / 1_000_000.0;
    }

    private static class UnionFind {
        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> rank = new HashMap<>();

        UnionFind(List<String> nodes) {
            for (String node : nodes) {
                parent.put(node, node);
                rank.put(node, 0);
            }
        }

        String find(String node) {
            String p = parent.get(node);
            if (!p.equals(node)) {
                String root = find(p);
                parent.put(node, root);
                return root;
            }
            return p;
        }

        void union(String a, String b) {
            String rootA = find(a);
            String rootB = find(b);
            if (rootA.equals(rootB)) {
                return;
            }
            int rankA = rank.get(rootA);
            int rankB = rank.get(rootB);
            if (rankA < rankB) {
                parent.put(rootA, rootB);
            } else if (rankA > rankB) {
                parent.put(rootB, rootA);
            } else {
                parent.put(rootB, rootA);
                rank.put(rootA, rankA + 1);
            }
        }
    }
}
