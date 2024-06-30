package logs;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static util.Files.readFile;
import static util.SimpleLog.printError;

public class Reader {

    private final String path;
    // "POST users" -> "GET users" -> 200 -> 2
    private Map<String, Map<String, Map<Integer, Integer>>> numberOfTimesEdgesOccurred;

    public Reader(String path) {
        this.path = path;
    }



    public void read() {
        try {
            List<String> lines = readFile(path);
            handleSequentialLogs(lines);
        } catch (IllegalArgumentException | IOException | NoSuchFieldException e) {
            printError(e);
        }
    }

    /*
        This operation handles the logic of the creation of the edges of the workload graph.
        The log file is expected to exhibit a sequential execution of the application and that
        every line represents a request or a response, more complex files are not supported here.
        This means that if line n is a request line n+1 will be the response of that request.
        There is an exception case, where a request is made by one of the servers, which
        leads to having request and response lines between a client request and the response
        to the client.
        Examples: Client = A, Servers = S1 and S2, -> = request and response.
        A -> S1 -> A (i.e. client A requests an op to S1 and S1 returns the response)
        A -> S1 -> S2 -> S1 -> A (i.e. client A requests an op to the server S1, the server S1 requests
                                       something from S2, that responds to S1 and S1 sends the final
                                       response to A)
     */
    public void handleSequentialLogs(List<String> lines) throws IllegalArgumentException  {

        // Let's analyse the first line to know what kind of pattern is used by the log file.
        String previousLine = lines.remove(0);
        EntryPattern requestPattern = EntryPattern.getPattern(previousLine);
        EntryPattern responsePattern = EntryPattern.getResponsePattern(requestPattern);

        for (String line : lines) {
            LogEntry l = new LogEntry(line);


            previousLine = line;
        }
    }




}
