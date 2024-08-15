package logs;

import graph.MapGraph;
import graph.Weight;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static util.Files.readFile;
import static util.SimpleLog.printError;
import static util.SimpleLog.println;

public class LogInterpreter {

    private static final String CLIENTS_IP_PATH = "clients.txt";
    private String path;
    private List<String> clients;
    // "POST /users" -> 200 -> "GET /users/{userId}" -> 2
    private Map<String, Map<Integer, Map<String, Integer>>> numberOfTimesEdgesOccurred;
    private Map<String, List<LogEntry>> clientsLog;

    public LogInterpreter(String logPath, String clientsIpPath) throws IOException, NoSuchFieldException {
        init(logPath, clientsIpPath);
    }
    public LogInterpreter(String logPath) throws IOException, NoSuchFieldException {
        init(logPath, null);
    }

    private void init(String logPath, String clientsIpPath) throws IOException, NoSuchFieldException {
        this.path = logPath;
        this.numberOfTimesEdgesOccurred = new HashMap<>();
        this.clientsLog = new HashMap<>();
        String clientsPath = clientsIpPath == null ? CLIENTS_IP_PATH : clientsIpPath;
        clients = readFile(clientsPath);
    }

    /* Reads access log file and stores each clients log in separate
     To keep causality of operations
     */
    public void interpret() {
        try {
            List<String> lines = readFile(path);
            if (lines.isEmpty()) {
                println("Logs are empty.");
                return;
            }
            for (String line : lines) {
                LogEntry logEntry = new LogEntry(line);
                if (!logEntry.isLogEntry()) continue;
                String clientIp = logEntry.getClientIp();
                if (!clients.contains(clientIp)) continue;
                clientsLog.computeIfAbsent(clientIp, k -> new LinkedList<>());
                clientsLog.get(clientIp).add(logEntry);
            }

            handleLogs();
        } catch (IllegalArgumentException | IOException | NoSuchFieldException e) {
            printError(e);
        }
    }

    private void handleLogs() {
        for (String client : clients) {
            handleSequentialLogs(clientsLog.get(client));
        }
    }

    /*
        This operation handles the logic of the creation of the edges of the workload graph.
        The log file is expected to exhibit a sequential execution of the application and
        every line represents a request or a response, more complex logs are not supported here.
        This means that if line n is a request line n+1 will be the response of that request.
        There is an exception case, where a request is made by one of the servers, which
        leads to having request and response lines between a client request and the response
        to the client.
        Examples: Note: Client = A, Servers = S1 and S2, -> = request and response.
        A -> S1 -> A (i.e. client A requests an op to S1 and S1 returns the response)
        A -> S1 -> S2 -> S1 -> A (i.e. client A requests an op to the server S1, the server S1 requests
                                       something from S2 that responds to S1 and S1 sends the final
                                       response to A)
     */
    private void handleSequentialLogs(List<LogEntry> lines) throws IllegalArgumentException  {
        if (lines == null) return;
        LogEntry previousEntry = lines.remove(0);
        for (LogEntry succeedingEntry : lines) {
            String previousVertexId = previousEntry.getVertexId();
            String succeedingVertexId = succeedingEntry.getVertexId();
            if (previousEntry.isResponse()) { // previousEntry is the response and succeedingEntry is the request
                Map<Integer, Map<String, Integer>> map1 = numberOfTimesEdgesOccurred.computeIfAbsent(previousVertexId, k -> new HashMap<>());
                Map<String, Integer> map2 = map1.computeIfAbsent(previousEntry.getStatus(), k -> new HashMap<>());
                map2.merge(succeedingVertexId, 1, Integer::sum);
                map1.put(previousEntry.getStatus(), map2);
                numberOfTimesEdgesOccurred.put(previousVertexId, map1);
            } else if (previousEntry.isRequest()) { // previousEntry is the request and succeedingEntry is the response
                /* ignore */
            }
            previousEntry = succeedingEntry;
        }
        //println("Map: " + numberOfTimesEdgesOccurred.toString());
    }

    public void fillGraph(MapGraph graph) {
        for (String vertexId : numberOfTimesEdgesOccurred.keySet()) {
            Map<Integer, Map<String, Integer>> map1 = numberOfTimesEdgesOccurred.get(vertexId);
            for (Integer status : map1.keySet()) {
                Map<String, Integer> map2 = map1.get(status);
                int total = map2.values().stream().mapToInt(Integer::intValue).sum();
                for (String nextVertexId : map2.keySet()) {
                    int weight = (int) Math.round((double) map2.get(nextVertexId) / total * 100);
                    graph.addEdge(vertexId, nextVertexId, status, new Weight((byte) weight));
                    //println("Edge: " + vertexId + ", status:" + status + " -> " + nextVertexId + " has weight: " + weight);
                }
            }
        }
    }

}
