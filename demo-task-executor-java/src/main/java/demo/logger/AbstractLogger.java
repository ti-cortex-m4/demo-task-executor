package demo.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract logger that allows to add  prefix to messages.
 * This class IS NOT thread-safe.
 */
abstract class AbstractLogger implements Logger {

    private static final String PATTERN = "hh:mm:ss.SSS";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    protected String getMessagePrefix(Severity severity) {
        StringBuilder sb = new StringBuilder();
        sb.append(LocalDateTime.now().format(FORMATTER));
        sb.append(" ");
        sb.append(severity);
        sb.append(" [");
        sb.append(Thread.currentThread().getName());
        sb.append("] ");
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(Object message) {
        log(Severity.TRACE, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(Object message) {
        log(Severity.DEBUG, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(Object message) {
        log(Severity.INFO, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(Object message) {
        log(Severity.WARN, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Object message) {
        log(Severity.ERROR, message);
    }
}
