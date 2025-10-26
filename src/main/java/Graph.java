import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable representation of a transportation network graph.
 */
public class Graph {
    private final String id;
    private final List<String> nodes;
    private final List<Edge> edges;

    @JsonCreator
    public Graph(@JsonProperty("id") String id,
                 @JsonProperty("nodes") List<String> nodes,
                 @JsonProperty("edges") List<Edge> edges) {
        this.id = String.valueOf(id);
        this.nodes = nodes == null ? List.of() : List.copyOf(nodes);
        if (edges == null) {
            this.edges = List.of();
        } else {
            List<Edge> safeCopy = new ArrayList<>(edges.size());
            for (Edge edge : edges) {
                safeCopy.add(edge);
            }
            this.edges = Collections.unmodifiableList(safeCopy);
        }
    }

    public String getId() {
        return id;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int vertexCount() {
        return nodes.size();
    }

    public int edgeCount() {
        return edges.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Graph graph = (Graph) o;
        return Objects.equals(id, graph.id) &&
                Objects.equals(nodes, graph.nodes) &&
                Objects.equals(edges, graph.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nodes, edges);
    }
}
