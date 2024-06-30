package logs;

public enum EntryPattern {

    SIMPLE_REQUEST_PATTERN("\\[(.+):(.+)\\] (.+) - (POST|GET|PUT|PATCH|DELETE) (.+) params=\\{(.*)\\} query=\\{(.*)\\}"),
    REQUEST_PATTERN_NO_BODY(SIMPLE_REQUEST_PATTERN.label + " headers=\\{(.*)\\}"),
    REQUEST_PATTERN(REQUEST_PATTERN_NO_BODY.label + " length=(.*) body=(.*)"),
    SIMPLE_RESPONSE_PATTERN("\\[(.+):(.+)\\] (.+) - (POST|GET|PUT|PATCH|DELETE) (.+) ([0-9]{3}) params=\\{(.*)\\} query=\\{(.*)\\}"),
    RESPONSE_PATTERN_NO_BODY(SIMPLE_RESPONSE_PATTERN.label + " headers=\\{(.*)\\}"),
    RESPONSE_PATTERN(RESPONSE_PATTERN_NO_BODY.label + " length=(.*) body=(.*)");

    public final String label;
    EntryPattern(String s) {
        this.label = s;
    }

    public static EntryPattern getPattern(String s) {
        for (EntryPattern p : EntryPattern.values()) {
            if (p.containsPattern(s, p.label))
                return p;
        }
        return null;
    }

    private boolean containsPattern(String line, String pattern) {
        return line.matches(pattern);
    }

    // return the respective response pattern knowing the request pattern
    public static EntryPattern getResponsePattern(EntryPattern requestPattern) throws IllegalArgumentException {
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

}
