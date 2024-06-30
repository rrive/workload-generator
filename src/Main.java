import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import graph.*;
import io.swagger.v3.oas.annotations.Operation;
import logs.Reader;

import javax.ws.rs.*;

import static util.Files.*;
import static util.SimpleLog.*;

public class Main {

    // TODO: LOGS_PATH should be used as an arg of the main method.
    private static final String LOGS_PATH = "../fct/sd/sd2324-proj-main/sd2324-tp1/logs/access.log";
    private static final String SPECIFICATION_PATH = "src/specification/";
    private static final String JEPREST_BASE_PATH = "JepRest/" + SPECIFICATION_PATH;
    private static final String JEPREST_ANNOTATIONS = "custom_annotations";

    private static void copyJepRESTCustomAnnotations(String jeprestPath) throws IOException, InterruptedException {
        copyDirectory(jeprestPath + JEPREST_ANNOTATIONS, SPECIFICATION_PATH + JEPREST_ANNOTATIONS);
    }

    private static void copyProjectInterfaces(String projectDirectory, String projectName) throws IOException, InterruptedException {
        copyDirectory(projectDirectory, SPECIFICATION_PATH + projectName);
    }

    private static String getMethodId(Method method) {
        Operation op = method.getAnnotation(Operation.class);
        if (op != null) {
            return op.operationId();
        } else {
            println("Annotation Operation is not present in method " + method.getName() + ".");
            return null;
        }
    }

    private static Vertex generateVertex(Method method, String basePath) {
        String path = basePath != null ? basePath : "";
        String httpVerb = "";
        Vertex vertex;
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (annotation instanceof Path) {
                path += ((Path) annotation).value();
            }
            if (annotation instanceof POST) {
                httpVerb = "POST";
            } else if (annotation instanceof GET) {
                httpVerb = "GET";
            } else if (annotation instanceof PUT) {
                httpVerb = "PUT";
            } else if (annotation instanceof PATCH) {
                httpVerb = "PUT";
            } else if (annotation instanceof DELETE) {
                httpVerb = "DELETE";
            }
        }
        /*int numPathParams = 0;
        for (Parameter parameter : method.getParameters()) {
            Annotation[] pas = parameter.getDeclaredAnnotations();
            for (Annotation pa : pas) {
                if (pa instanceof PathParam) {
                    //println("Path param: " + ((PathParam) pa).value());
                    numPathParams++;
                }
            }
        }*/
        String operationId = getMethodId(method);
        vertex = new Vertex(operationId, httpVerb, path);
        return vertex;
    }

    private static MapGraph addServicesInterfacesVertices(String projectName) throws ClassNotFoundException {
        File[] files = getFiles(SPECIFICATION_PATH + projectName);
        List<Vertex> vertices = new LinkedList<>();
        for (File file : files) {
            if (file.isDirectory()) continue;
            Class<?> interface_ = getInterfaceFromFile(file, projectName);
            if (interface_ == null || interface_.getDeclaredMethods().length == 0) continue;
            println(file.getName());
            Path basePathAnnotation = interface_.getAnnotation(Path.class);
            String basePath = basePathAnnotation != null ? basePathAnnotation.value() : "";
            for (Method method : interface_.getMethods()) {
                Vertex vertex = generateVertex(method, basePath);
                println(vertex.toString());
                if (vertex.getOpId() != null)
                    vertices.add(vertex);
            }
        }
        return new MapGraph(vertices);
    }

    private static MapGraph generateGraphFromInterfaces(String projectName)
            throws ClassNotFoundException, IOException {
        return addServicesInterfacesVertices(projectName);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length < 2){
            println("  usage: <project directory path> <project name>");
            println("  notes: <project directory path> = if using JepREST \"path/to/JepREST\", else \"path/to/project/services/interfaces\"\n" +
                    "                   <project name> = project name (╯°□°）╯︵ ┻━┻");
            println("example: ../JepREST sd");
            System.exit(1);
        }
        String projectDirectory = args[0];
        String projectName = args[1];
        if(projectDirectory.toLowerCase().contains("jeprest")) {
            // Using workload generator for JepREST testing.
            if (projectDirectory.endsWith("/"))
                projectDirectory += JEPREST_BASE_PATH;
            else projectDirectory += "/" + JEPREST_BASE_PATH;
            copyJepRESTCustomAnnotations(projectDirectory);
        }
        projectDirectory += projectName + "/";
        copyProjectInterfaces(projectDirectory, projectName);

        Reader reader = new Reader(LOGS_PATH);
        reader.read();

        /*MapGraph graph;
        try {
            graph = generateGraphFromInterfaces(projectName);
            println(graph.toString());
            graph.addEdge("createUser", "getUser", 200, new Weight((byte) 40));
            graph.addEdge("createUser", "updateUser", 200, new Weight((byte) 60));
            graph.addEdge("getUser", "updateUser", 200, new Weight((byte) 20));
            graph.addEdge("getUser", "getUser", 200, new Weight((byte) 80));
            println(graph.toString());
            println(graph.getNextVertex("getUser", 200));
        } catch (ClassNotFoundException | IOException e) {
            printError(e);
        }*/







        // TODO: Read logs to generate edges and weight

    }

}
