import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic statistics about an input graph.
 */
public class InputStats {
    @JsonProperty("vertices")
    private final int vertices;

    @JsonProperty("edges")
    private final int edges;

    public InputStats(int vertices, int edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public int getVertices() {
        return vertices;
    }

    public int getEdges() {
        return edges;
    }
}
