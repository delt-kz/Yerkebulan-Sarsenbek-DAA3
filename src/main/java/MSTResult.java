import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Holds the output of an MST algorithm execution.
 */
public class MSTResult {
    @JsonProperty("mst_edges")
    private final List<Edge> mstEdges;

    @JsonProperty("total_cost")
    private final double totalCost;

    @JsonProperty("comparisons_count")
    private final long comparisonsCount;

    @JsonProperty("updates_count")
    private final long updatesCount;

    @JsonProperty("unions_count")
    private final long unionsCount;

    @JsonProperty("operations_count")
    private final long operationsCount;

    @JsonProperty("execution_time_ms")
    private final double executionTimeMs;

    @JsonProperty("connected")
    private final boolean connected;

    public MSTResult(List<Edge> mstEdges,
                     double totalCost,
                     long comparisonsCount,
                     long updatesCount,
                     long unionsCount,
                     double executionTimeMs,
                     boolean connected) {
        this.mstEdges = List.copyOf(mstEdges);
        this.totalCost = totalCost;
        this.comparisonsCount = comparisonsCount;
        this.updatesCount = updatesCount;
        this.unionsCount = unionsCount;
        this.operationsCount = comparisonsCount + updatesCount + unionsCount;
        this.executionTimeMs = executionTimeMs;
        this.connected = connected;
    }

    public List<Edge> getMstEdges() {
        return mstEdges;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public long getOperationsCount() {
        return operationsCount;
    }

    public long getComparisonsCount() {
        return comparisonsCount;
    }

    public long getUpdatesCount() {
        return updatesCount;
    }

    public long getUnionsCount() {
        return unionsCount;
    }

    public MSTResult withExecutionTime(double executionTimeMs) {
        return new MSTResult(mstEdges, totalCost, comparisonsCount, updatesCount, unionsCount, executionTimeMs, connected);
    }

    public double getExecutionTimeMs() {
        return executionTimeMs;
    }

    public boolean isConnected() {
        return connected;
    }
}
