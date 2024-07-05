package graph;

import java.util.regex.Pattern;

public class Vertex {

    private static final String VERTEX_ID_PATTERN = "(POST|GET|PUT|PATCH|DELETE)\\s(\\S+)";
    private static final Pattern pattern = Pattern.compile(VERTEX_ID_PATTERN);
    private final String vertexId;
    private final String opId;
    private final String method;
    private final String path;

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

    // return tru if the parameter v matches the pattern of a vertex id, else false.
    public static boolean isVertexId(String v) {
        return pattern.matcher(v).matches();
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
