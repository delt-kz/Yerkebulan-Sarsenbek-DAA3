import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds the results of running Prim's and Kruskal's algorithms on a graph.
 */
public class GraphComputationResult {
    @JsonProperty("graph_id")
    private final String graphId;

    @JsonProperty("input_stats")
    private final InputStats inputStats;

    @JsonProperty("prim")
    private final MSTResult primResult;

    @JsonProperty("kruskal")
    private final MSTResult kruskalResult;

    public GraphComputationResult(String graphId, InputStats inputStats, MSTResult primResult, MSTResult kruskalResult) {
        this.graphId = graphId;
        this.inputStats = inputStats;
        this.primResult = primResult;
        this.kruskalResult = kruskalResult;
    }

    public String getGraphId() {
        return graphId;
    }

    public InputStats getInputStats() {
        return inputStats;
    }

    public MSTResult getPrimResult() {
        return primResult;
    }

    public MSTResult getKruskalResult() {
        return kruskalResult;
    }
}
