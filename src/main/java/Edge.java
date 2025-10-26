import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Represents an undirected weighted edge between two districts in the transportation network.
 */
public class Edge {
    private final String from;
    private final String to;
    private final double weight;

    @JsonCreator
    public Edge(@JsonProperty("from") String from,
                @JsonProperty("to") String to,
                @JsonProperty("weight") double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    public Edge reversed() {
        return new Edge(to, from, weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Double.compare(edge.weight, weight) == 0 &&
                Objects.equals(from, edge.from) &&
                Objects.equals(to, edge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, weight);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", weight=" + weight +
                '}';
    }
}
