package demo.logger;

/**
 * Logging severity.
 */
public enum Severity {

    TRACE(1),
    DEBUG(2),
    INFO(3),
    WARN(4),
    ERROR(5);

    private int value;

    Severity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
