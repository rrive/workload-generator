package graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.SimpleLog.println;

public class ArrayGraph /*implements Graph*/ {

    private byte[][] graph;
    private final Map<String, Integer> index;

    public ArrayGraph() {
        index = new HashMap<>();
    }

    public ArrayGraph(List<String> vertices) {
        index = new HashMap<>();
        graph = new byte[vertices.size()][vertices.size()];
        addVertices(vertices);
    }

    private int generateIndex(String vertex) {
        if (index.isEmpty())
            return 0;
        return Collections.max(index.values()) + 1;
    }

    public void addVertex(String v) {
        //println("Added vertex: " + v);
        int i = generateIndex(v);
        index.put(v, i);
    }

    public void addVertices(List<String> vs) {
        for (String v : vs)
            addVertex(v);
    }

    public void addEdge(String v1, String v2) {

    }

    public void addEdge(String v1, String v2, int code, Weight weight) {/* unused */}

    public void addWeight(String v1, String v2, byte weight) {

    }

    public String toString() {
        String str = "";
        for (byte[] vs : graph) {
            str += "\n";
            for (byte v : vs) {
                str += v + "     ";
            }
        }
        return str;
    }

}
