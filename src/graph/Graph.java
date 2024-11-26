package graph;

import java.io.Serializable;
import java.util.*;

public class Graph implements Serializable {

    private Random random;

    // key = method + path, value = vertex
    Map<String, Vertex> vertices;
    Map<String, Map<Integer, TreeSet<Edge>>> graph;
    Map<String, Integer> starterVertices;

    public Graph() {
        instantiateGraph();
    }

    public Graph(List<Vertex> vs) {
        instantiateGraph();
        addVertices(vs);
    }

    private void instantiateGraph() {
        vertices = new HashMap<>();
        graph  = new HashMap<>();
        random = new Random(System.currentTimeMillis());
        starterVertices = new HashMap<>();
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
        String opId1 = Vertex.isVertexId(v1) ? vertices.get(v1).getOpId() : v1;
        String opId2 = Vertex.isVertexId(v2) ? vertices.get(v2).getOpId() : v2;
        areVertices(opId1, opId2);
        Edge edge = new Edge(opId1, opId2, w);
        TreeSet<Edge> edges = graph.get(opId1).get(code);
        if (edges == null)
            edges = new TreeSet<>(Comparator.naturalOrder());
        edges.add(edge);
        graph.get(opId1).put(code, edges);
    }

    public void addStartingVertex(String v, int prob) {
        String opId = Vertex.isVertexId(v) ? vertices.get(v).getOpId() : v;
        starterVertices.put(opId, prob);
    }

    public String getFirstVertex() {
        int r = random.nextInt(100);
        Set<Map.Entry<String, Integer>> starterVerticesSet = starterVertices.entrySet();
        //println("Starter vertices set " + starterVerticesSet);
        if (!starterVerticesSet.isEmpty())
            for (Map.Entry<String, Integer> starterVertex : starterVerticesSet) {
                if (r <= starterVertex.getValue()) {
                    return starterVertex.getKey();
                }
                r -= starterVertex.getValue();
            }
        return null;
    }

    public String getNextVertex(String v, int code) {
        int r = random.nextInt(100);
        //println("Random number: " + r);
        Map<Integer, TreeSet<Edge>> vertexInfo = graph.get(v);
        if (vertexInfo == null) return null;
        TreeSet<Edge> edges = vertexInfo.get(code);
        if (edges != null)
            for (Edge edge : edges) {
                //println(edge.toString());
                if (r <= edge.getWeight().getWeight()) {
                    //println("random " + r+ " <= " + edge.getWeight().getWeight() + " weight");
                    return edge.getDestination();
                }
                r -= edge.getWeight().getWeight();
            }
        // If it gets here, no edges were found for the given code.
        // Todo: think how to handle this case.
        // Options: return a random vertex from the graph,
        //          return a random vertex from any edge that has the same source vertex as v,
        // (actual) return null an let JepREST handle it (e.g. JepREST starts new execution from the beginning),
        //          what else?
        return null;
    }

    @Override
    public String toString() {
        return graph.toString();
    }

}
