package logs;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.SimpleLog.println;

public class LogEntry {

    private String line;
    private boolean isRequest;
    private String requestId, host, date, method, endpoint;
    private EntryPattern pattern;

    //todo: how to represent params and query? params is a MultivaluedMap and query is a string so take a look at that

    public LogEntry(String line) {
        this.line = line;
        interpretLine();
    }

    private void interpretLine() {
        pattern = EntryPattern.getPattern(line);
        if (pattern == null) {
            println("Log line doesn't match any supported pattern. Line: " + line);
            return;
        }
        isRequest = isRequest(line);


        Pattern p = Pattern.compile(pattern.label);
        Matcher matcher = p.matcher(line);
        if (matcher.find()) {
            println(matcher.group(4));
        }

    }

    private String generateRequestId() {
        return pattern.label;
    }

    private boolean isRequest(String line) throws IllegalArgumentException, NullPointerException {
        switch (Objects.requireNonNull(EntryPattern.getPattern(line))) {
            case REQUEST_PATTERN:
            case REQUEST_PATTERN_NO_BODY:
            case SIMPLE_REQUEST_PATTERN:
                return true;
            case RESPONSE_PATTERN:
            case RESPONSE_PATTERN_NO_BODY:
            case SIMPLE_RESPONSE_PATTERN:
                return false;
        }
        throw new IllegalArgumentException("The pattern used in the logs is not supported.");
    }


}
