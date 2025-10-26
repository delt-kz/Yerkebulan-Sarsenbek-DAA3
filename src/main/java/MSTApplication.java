import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Entry point that loads graphs, runs MST algorithms, and writes results and comparison tables.
 */
public final class MSTApplication {

    private MSTApplication() {
    }

    public static void main(String[] args) throws IOException {
        Path input = args.length > 0 ? Path.of(args[0]) : Path.of("src/main/resources/datasets/assign_3_input.json");
        Path output = args.length > 1 ? Path.of(args[1]) : Path.of("results/output.json");
        Path csv = args.length > 2 ? Path.of(args[2]) : Path.of("results/comparison.csv");

        GraphDataset dataset = GraphIO.readDataset(input);
        MSTCalculator calculator = new MSTCalculator();
        List<GraphComputationResult> results = new ArrayList<>();
        int warmupRuns = parseRuns(System.getProperty("mst.warmupRuns"), 3, 0);
        int measurementRuns = parseRuns(System.getProperty("mst.measurementRuns"), 7, 1);

        for (Graph graph : dataset.getGraphs()) {
            MSTResult prim = runWithStabilizedTiming(() -> calculator.computePrim(graph), warmupRuns, measurementRuns);
            MSTResult kruskal = runWithStabilizedTiming(() -> calculator.computeKruskal(graph), warmupRuns, measurementRuns);
            InputStats stats = new InputStats(graph.vertexCount(), graph.edgeCount());
            results.add(new GraphComputationResult(graph.getId(), stats, prim, kruskal));
        }

        if (output.getParent() != null) {
            Files.createDirectories(output.getParent());
        }
        if (csv.getParent() != null) {
            Files.createDirectories(csv.getParent());
        }
        GraphIO.writeResults(output, results);
        GraphIO.writeComparisonCsv(csv, results);
    }

    private static MSTResult runWithStabilizedTiming(Supplier<MSTResult> computation, int warmupRuns, int measurementRuns) {
        MSTResult last = null;
        for (int i = 0; i < warmupRuns; i++) {
            last = computation.get();
        }

        double[] samples = new double[measurementRuns];
        for (int i = 0; i < measurementRuns; i++) {
            last = computation.get();
            samples[i] = last.getExecutionTimeMs();
        }

        if (last == null) {
            throw new IllegalStateException("MST computation was never executed");
        }

        Arrays.sort(samples);
        double stabilizedTime;
        int middle = measurementRuns / 2;
        if (measurementRuns % 2 == 0) {
            stabilizedTime = (samples[middle - 1] + samples[middle]) / 2.0;
        } else {
            stabilizedTime = samples[middle];
        }

        return last.withExecutionTime(stabilizedTime);
    }

    private static int parseRuns(String property, int defaultValue, int minimumValue) {
        if (property == null) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(property.trim());
            return Math.max(minimumValue, parsed);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
