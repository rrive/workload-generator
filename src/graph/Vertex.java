package graph;

public class Vertex {

    private String vertexId;
    private String opId;
    private String method;
    private String path;

    public Vertex(String opId, String method, String path) {
        this.opId = opId;
        this.method = method;
        this.path = path;
        this.vertexId = method + " " +  path;
    }

    public String getOpId() {
        return opId;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVertexId() {
        return vertexId;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "vertexId='" + vertexId + '\'' +
                ", opId='" + opId + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

}
