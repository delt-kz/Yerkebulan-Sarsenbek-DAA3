import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Utility methods for reading input graphs and writing results.
 */
public final class GraphIO {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private GraphIO() {
    }

    public static GraphDataset readDataset(Path path) throws IOException {
        try (var reader = Files.newBufferedReader(path)) {
            return MAPPER.readValue(reader, GraphDataset.class);
        }
    }

    public static void writeResults(Path path, List<GraphComputationResult> results) throws IOException {
        ObjectWriter writer = MAPPER.writer().without(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        try (var output = Files.newBufferedWriter(path)) {
            writer.writeValue(output, new ResultsWrapper(results));
        }
    }

    public static void writeComparisonCsv(Path path, List<GraphComputationResult> results) throws IOException {
        try (var writer = Files.newBufferedWriter(path)) {
            writer.write("graph_id,prim_total_cost,kruskal_total_cost,prim_execution_time_ms,kruskal_execution_time_ms,"
                    + "prim_comparisons,prim_updates,prim_unions,prim_operations,"
                    + "kruskal_comparisons,kruskal_updates,kruskal_unions,kruskal_operations\n");
            for (GraphComputationResult result : results) {
                MSTResult prim = result.getPrimResult();
                MSTResult kruskal = result.getKruskalResult();
                writer.write(String.format(Locale.US,
                        "%s,%.3f,%.3f,%.3f,%.3f,%d,%d,%d,%d,%d,%d,%d,%d%n",
                        result.getGraphId(),
                        prim.getTotalCost(),
                        kruskal.getTotalCost(),
                        prim.getExecutionTimeMs(),
                        kruskal.getExecutionTimeMs(),
                        prim.getComparisonsCount(),
                        prim.getUpdatesCount(),
                        prim.getUnionsCount(),
                        prim.getOperationsCount(),
                        kruskal.getComparisonsCount(),
                        kruskal.getUpdatesCount(),
                        kruskal.getUnionsCount(),
                        kruskal.getOperationsCount()));
            }
        }
    }

    private record ResultsWrapper(@com.fasterxml.jackson.annotation.JsonProperty("results")
                                  List<GraphComputationResult> results) {
    }
}
