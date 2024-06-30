package graph;

import java.util.*;

import static util.SimpleLog.println;

public class MapGraph {

    private Random random;

    // key = method + path, value = vertex
    Map<String, Vertex> vertices;
    Map<String, Map<Integer, TreeSet<Edge>>> graph;

    public MapGraph() {
        instantiateGraph();
    }

    public MapGraph(List<Vertex> vs) {
        instantiateGraph();
        addVertices(vs);
    }

    private void instantiateGraph() {
        vertices = new HashMap<>();
        graph  = new HashMap<>();
        random = new Random(System.currentTimeMillis());
    }

    public void addVertex(Vertex v) {
        vertices.put(v.getVertexId(), v);
        graph.put(v.getOpId(), new HashMap<>());
    }

    public void addVertices(List<Vertex> vs) {
        for (Vertex v : vs)
            addVertex(v);
    }

    public boolean isVertex(String v) {
        return graph.containsKey(v);
    }

    private void areVertices(String... vs) throws IllegalArgumentException {
        List<String> illegalVertices = new LinkedList<>();
        for (String v : vs)
            if (!isVertex(v))
                illegalVertices.add(v);
        if (!illegalVertices.isEmpty())
            throw new IllegalArgumentException("Vertices " + illegalVertices + " are not in the graph.");
    }

    public void addEdge(String v1, String v2, int code, Weight w) {
        areVertices(v1, v2);
        Edge edge = new Edge(v1, v2, w);
        TreeSet<Edge> edges = graph.get(v1).get(code);
        if (edges == null)
            edges = new TreeSet<>(Comparator.naturalOrder());
        edges.add(edge);
        graph.get(v1).put(code, edges);
    }

    public void addWeight(String v1, String v2, byte weight) {

    }

    public String getNextVertex(String v, int code) {
        int r = random.nextInt(101);
        println("Random number: " + r);
        TreeSet<Edge> edges = graph.get(v).get(code);
        if (edges != null)
            for (Edge edge : edges) {
                println(edge.toString());
                if (r <= edge.getWeight().getWeight()) {
                    println("random " + r+ " <= " + edge.getWeight().getWeight() + " weight");
                    return edge.getDestination();
                }
                r -= edge.getWeight().getWeight();
            }
        // If it gets here, no edges were found for the given code.
        // Todo: think how to handle this case.
        // Options: return a random vertex from the graph,
        //          return a random vertex from any edge that has the same source vertex as v,
        //          return null an let JepREST handle it (e.g. JepREST starts new execution from the beginning),
        //          what else?
        return null;
    }

    @Override
    public String toString() {
        return graph.toString();
    }


}
