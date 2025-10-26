import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple counter used to track algorithmic operations such as comparisons and unions.
 */
public class OperationCounter {
    private final AtomicLong comparisons = new AtomicLong();
    private final AtomicLong updates = new AtomicLong();
    private final AtomicLong unions = new AtomicLong();
    private volatile boolean unionOperationsRecorded;

    public void incrementComparison() {
        comparisons.incrementAndGet();
    }

    public void incrementUpdate() {
        updates.incrementAndGet();
    }

    public void incrementUnion() {
        unions.incrementAndGet();
        unionOperationsRecorded = true;
    }

    public long getTotalOperations() {
        return comparisons.get() + updates.get() + unions.get();
    }

    public long getComparisons() {
        return comparisons.get();
    }

    public long getUpdates() {
        return updates.get();
    }

    public long getUnions() {
        return unions.get();
    }

    public boolean hasUnionOperations() {
        return unionOperationsRecorded;
    }
}
