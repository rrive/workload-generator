package logs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.SimpleLog.println;

public class LogEntry {

    private final String PARAMS_PATTERN = "\\s*([^={]+)=([^,}]+),?\\s*";
    private final Pattern paramsRegex = Pattern.compile(PARAMS_PATTERN);
    private final String line;
    private boolean isRequest;
    private String requestId, clientIP, date, method, endpoint, params, query, headers, body, vertexId;
    private int status, length;
    private LogEntryPattern pattern;

    //todo: how to represent params and query? params is a MultivaluedMap and query is a string so take a look at that

    public LogEntry(String line) {
        this.line = line;
        interpretLine();
    }

    private void interpretLine() {
        pattern = LogEntryPattern.getPattern(line);
        if (pattern == null) {
            println("Log line doesn't match any supported pattern. Line: " + line);
            return;
        }
        isRequest = isRequest(pattern);
        Pattern p = Pattern.compile(pattern.label);
        Matcher matcher = p.matcher(line);
        int i = 1;
        if (matcher.find()) {
            requestId = matcher.group(i++);
            clientIP = matcher.group(i++);
            date = matcher.group(i++);
            method = matcher.group(i++);
            endpoint = matcher.group(i++);
            if (!isRequest)
                status = Integer.parseInt(matcher.group(i++));
            params = matcher.group(i++);
            query = matcher.group(i++);
            if (hasHeaders(pattern))
                headers = matcher.group(i++);
            if (hasBody(pattern)) {
                length = Integer.parseInt(matcher.group(i++));
                body = matcher.group(i);
            }
            generateVertexId();
        }
    }

    /*
    *  Example: GET request to endpoint "users/John-1", where "John-1" is the value of the param "userId",
    *           becomes a vertex identifier: "GET /users/{userId}"
     */
    private void generateVertexId() {
        Matcher m = paramsRegex.matcher(params);
        String path = endpoint;
        while (m.find()) {
            String key = m.group(1);
            String value = m.group(2).replace("[", "").replace("]", "");
            path = path.replace(value, "{" + key + "}");
        }
        vertexId = method + " " + "/" + path;
        //println("Generated vertexId = " + vertexId);
    }

    private boolean isRequest(LogEntryPattern p) throws IllegalArgumentException, NullPointerException {
        return p.equals(LogEntryPattern.SIMPLE_REQUEST_PATTERN) || p.equals(LogEntryPattern.REQUEST_PATTERN)
                || p.equals(LogEntryPattern.REQUEST_PATTERN_NO_BODY);
    }

    private boolean isResponse(LogEntryPattern p) throws IllegalArgumentException, NullPointerException {
        return p.equals(LogEntryPattern.SIMPLE_RESPONSE_PATTERN) || p.equals(LogEntryPattern.RESPONSE_PATTERN)
                || p.equals(LogEntryPattern.RESPONSE_PATTERN_NO_BODY);
    }

    private boolean hasHeaders(LogEntryPattern p) {
        return p.equals(LogEntryPattern.RESPONSE_PATTERN_NO_BODY) || p.equals(LogEntryPattern.REQUEST_PATTERN_NO_BODY);
    }

    private boolean hasBody(LogEntryPattern p) {
        return p.equals(LogEntryPattern.REQUEST_PATTERN) || p.equals(LogEntryPattern.RESPONSE_PATTERN);
    }

    public boolean isLogEntry() {
        return pattern != null;
    }

    /*
    *  GETTERS
    * */

    public String getVertexId() {
        return vertexId;
    }

    public String getLine() {
        return line;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public boolean isResponse() {
        return !isRequest;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getClientIp() {
        return clientIP;
    }

    public String getDate() {
        return date;
    }

    public String getMethod() {
        return method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    // Todo: return multivaluedMap
    public String getParamsString() {
        return params;
    }

    // Todo: return map?
    public String getQuery() {
        return query;
    }

    public String getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public int getStatus() {
        return status;
    }

    public int getLength() {
        return length;
    }

    public LogEntryPattern getPattern() {
        return pattern;
    }


    @Override
    public String toString() {
        return "LogEntry{" +
                "line='" + line + '\'' +
                ", isRequest=" + isRequest +
                ", requestId='" + requestId + '\'' +
                ", clientIP='" + clientIP + '\'' +
                ", date='" + date + '\'' +
                ", method='" + method + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", params='" + params + '\'' +
                ", query='" + query + '\'' +
                ", headers='" + headers + '\'' +
                ", body='" + body + '\'' +
                ", vertexId='" + vertexId + '\'' +
                ", status=" + status +
                ", length=" + length +
                ", pattern=" + pattern +
                '}';
    }
}
