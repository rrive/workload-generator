package graph;

public class Edge implements Comparable<Edge> {

    private final String source, destination;
    private Weight weight;

    public Edge(String source, String destination, Weight weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return source + " -> " + destination + " : " + weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Byte.compare(this.weight.getWeight(), other.getWeight().getWeight());
    }
}