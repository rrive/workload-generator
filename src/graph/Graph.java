package graph;

import java.util.List;

public interface Graph {

    void addVertex(String v);

    void addVertices(List<String> vs);

    boolean isVertex(String v);

    void addEdge(String v1, String v2);

    void addEdge(String v1, String v2, int code, Weight w);

    void addWeight(String v1, String v2, byte weight);

    String getNextVertex(String v, int code);
}
