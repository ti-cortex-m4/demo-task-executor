package demo.logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Factory that builds a logger facade that writes messages to many {@link demo.logger.Logger} simultaneously.
 * This class IS thread-safe.
 */
final public class LoggerFactory {

    private final String className;
    private final Map<Logger, Severity> loggers;

    /**
     * Creates the logging factory for a client class.
     *
     * @param clazz the client clazz.
     */
    public LoggerFactory(Class<?> clazz) {
        this.className = Objects.requireNonNull(clazz).getName();
        this.loggers = new HashMap<>();
    }

    /**
     * Add a logger to the logging factory.
     *
     * @param logger   the logger.
     * @param severity the logging severity for the logger.
     */
    synchronized public LoggerFactory add(Logger logger, Severity severity) {
        loggers.put(Objects.requireNonNull(logger), Objects.requireNonNull(severity));
        return this;
    }

    /**
     * Builds a thread-safe logger facade from the added loggers.
     *
     * @return the thread-safe logger facade.
     */
    public Logger build() {
        return new AbstractLogger() {
            @Override
            synchronized public void log(Severity severity, Object message) {
                for (Logger logger : loggers.keySet()) {
                    if (severity.getValue() >= loggers.get(logger).getValue()) {
                        logger.log(severity, className + " - " + message);
                    }
                }
            }
        };
    }
}
