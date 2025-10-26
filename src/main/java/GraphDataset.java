import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Container that reflects the structure of the JSON dataset file.
 */
public class GraphDataset {
    private final List<Graph> graphs;

    @JsonCreator
    public GraphDataset(@JsonProperty("graphs") List<Graph> graphs) {
        this.graphs = graphs == null ? List.of() : List.copyOf(graphs);
    }

    public List<Graph> getGraphs() {
        return graphs;
    }
}
