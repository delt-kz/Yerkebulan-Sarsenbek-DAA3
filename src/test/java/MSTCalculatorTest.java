import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MSTCalculatorTest {

    private final MSTCalculator calculator = new MSTCalculator();

    @Test
    void primAndKruskalProduceIdenticalTotalCost() {
        Graph graph = buildMediumGraph();

        MSTResult prim = calculator.computePrim(graph);
        MSTResult kruskal = calculator.computeKruskal(graph);

        assertEquals(prim.getTotalCost(), kruskal.getTotalCost(), 1e-9, "Total costs must match");
    }

    @Test
    void mstContainsVMinusOneEdgesWhenConnected() {
        Graph graph = buildMediumGraph();
        int expectedEdges = graph.vertexCount() - 1;

        MSTResult prim = calculator.computePrim(graph);
        MSTResult kruskal = calculator.computeKruskal(graph);

        assertEquals(expectedEdges, prim.getMstEdges().size());
        assertEquals(expectedEdges, kruskal.getMstEdges().size());
    }

    @Test
    void mstIsAcyclicAndSpansAllVertices() {
        Graph graph = buildMediumGraph();

        MSTResult prim = calculator.computePrim(graph);
        MSTResult kruskal = calculator.computeKruskal(graph);

        assertTrue(prim.isConnected());
        assertTrue(kruskal.isConnected());
        assertTrue(isAcyclic(graph.getNodes(), prim.getMstEdges()));
        assertTrue(isAcyclic(graph.getNodes(), kruskal.getMstEdges()));
    }

    @Test
    void disconnectedGraphsAreHandledGracefully() {
        Graph graph = new Graph("disconnected",
                List.of("A", "B", "C", "D"),
                List.of(
                        new Edge("A", "B", 1),
                        new Edge("C", "D", 2)
                ));

        MSTResult prim = calculator.computePrim(graph);
        MSTResult kruskal = calculator.computeKruskal(graph);

        assertFalse(prim.isConnected());
        assertFalse(kruskal.isConnected());
        assertTrue(prim.getMstEdges().size() < graph.vertexCount() - 1);
        assertTrue(kruskal.getMstEdges().size() < graph.vertexCount() - 1);
    }

    @Test
    void repeatedRunsProduceConsistentResults() {
        Graph graph = buildMediumGraph();

        MSTResult firstPrim = calculator.computePrim(graph);
        MSTResult secondPrim = calculator.computePrim(graph);
        MSTResult firstKruskal = calculator.computeKruskal(graph);
        MSTResult secondKruskal = calculator.computeKruskal(graph);

        assertEquals(firstPrim.getTotalCost(), secondPrim.getTotalCost(), 1e-9);
        assertEquals(firstKruskal.getTotalCost(), secondKruskal.getTotalCost(), 1e-9);
        assertEquals(firstPrim.getOperationsCount(), secondPrim.getOperationsCount());
        assertEquals(firstKruskal.getOperationsCount(), secondKruskal.getOperationsCount());
        assertEquals(firstPrim.getComparisonsCount() + firstPrim.getUpdatesCount() + firstPrim.getUnionsCount(),
                firstPrim.getOperationsCount());
        assertEquals(firstKruskal.getComparisonsCount() + firstKruskal.getUpdatesCount() + firstKruskal.getUnionsCount(),
                firstKruskal.getOperationsCount());
        assertTrue(firstPrim.getExecutionTimeMs() >= 0.0);
        assertTrue(firstKruskal.getExecutionTimeMs() >= 0.0);
    }

    private Graph buildMediumGraph() {
        return new Graph("medium-test",
                List.of("A", "B", "C", "D", "E", "F"),
                List.of(
                        new Edge("A", "B", 4),
                        new Edge("A", "C", 3),
                        new Edge("B", "C", 1),
                        new Edge("B", "D", 2),
                        new Edge("C", "D", 4),
                        new Edge("C", "E", 6),
                        new Edge("D", "E", 5),
                        new Edge("D", "F", 3),
                        new Edge("E", "F", 2)
                ));
    }

    private boolean isAcyclic(List<String> nodes, List<Edge> edges) {
        UnionFind unionFind = new UnionFind(nodes);
        for (Edge edge : edges) {
            String rootU = unionFind.find(edge.getFrom());
            String rootV = unionFind.find(edge.getTo());
            if (rootU.equals(rootV)) {
                return false;
            }
            unionFind.union(rootU, rootV);
        }
        return true;
    }

    private static class UnionFind {
        private final java.util.Map<String, String> parent = new java.util.HashMap<>();
        private final java.util.Map<String, Integer> rank = new java.util.HashMap<>();

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
