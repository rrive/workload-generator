package logs;

public enum LogEntryPattern {

    // These patterns are used to identify the type of log entry
    // Note: The patterns are not perfect, they are just a simple way to identify the type of log entry,
    //       will be improved in the future.
    SIMPLE_REQUEST_PATTERN("\\s*\\[(\\S+):(\\S+|)]\\s+(.+)\\s+-\\s+(POST|GET|PUT|PATCH|DELETE)\\s+(\\S+)\\s+params=\\{(.*)}\\s+query=\\{(.*)}\\s*"),
    REQUEST_PATTERN_NO_BODY(SIMPLE_REQUEST_PATTERN.label + "\\s+headers=\\{(.*)}\\s*"),
    REQUEST_PATTERN(REQUEST_PATTERN_NO_BODY.label + "\\s+body=(.*)"),
    SIMPLE_RESPONSE_PATTERN("\\s*\\[(\\S+):(\\S+|)]\\s+(.+)\\s+-\\s+(POST|GET|PUT|PATCH|DELETE)\\s+(\\S+)\\s+([0-9]{3})\\s+params=\\{(.*)}\\s+query=\\{(.*)}\\s*"),
    RESPONSE_PATTERN_NO_BODY(SIMPLE_RESPONSE_PATTERN.label + "\\s+headers=\\{(.*)}\\s*"),
    RESPONSE_PATTERN(RESPONSE_PATTERN_NO_BODY.label + "\\s+body=(.*)");

    public final String label;
    LogEntryPattern(String s) {
        this.label = s;
    }

    public static LogEntryPattern getPattern(String s) {
        for (LogEntryPattern p : LogEntryPattern.values()) {
            if (p.containsPattern(s, p.label)) {
                return p;
            }
        }
        return null;
    }

    private boolean containsPattern(String line, String pattern) {
        return line.matches(pattern);
    }

    // return the respective response pattern knowing the request pattern
    public static LogEntryPattern getResponsePattern(LogEntryPattern requestPattern) throws IllegalArgumentException {
        switch (requestPattern) {
            case REQUEST_PATTERN:
                return RESPONSE_PATTERN;
            case REQUEST_PATTERN_NO_BODY:
                return RESPONSE_PATTERN_NO_BODY;
            case SIMPLE_REQUEST_PATTERN:
                return SIMPLE_RESPONSE_PATTERN;
        }
        throw new IllegalArgumentException("Unknown request pattern: " + requestPattern);
    }

    public boolean equals(LogEntryPattern p) {
        return this.label.equals(p.label);
    }

}
