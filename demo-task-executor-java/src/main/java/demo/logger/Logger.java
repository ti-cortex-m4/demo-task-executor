package demo.logger;

/**
 * Logger that allows to write messages with different severity.
 * Its implementations MUST be thread-safe.
 */
public interface Logger {

    /**
     * Writes a message with the given severity.
     *
     * @param severity the message severity.
     * @param message  the message.
     */
    void log(Severity severity, Object message);

    /**
     * Writes a message with the severity {@link demo.logger.Severity#TRACE}.
     *
     * @param message the message.
     */
    void trace(Object message);

    /**
     * Writes a message with the severity {@link demo.logger.Severity#DEBUG}.
     *
     * @param message the message.
     */
    void debug(Object message);

    /**
     * Writes a message with the severity {@link demo.logger.Severity#INFO}.
     *
     * @param message the message.
     */
    void info(Object message);

    /**
     * Writes a message with the severity {@link demo.logger.Severity#WARN}.
     *
     * @param message the message.
     */
    void warn(Object message);

    /**
     * Writes a message with the severity {@link demo.logger.Severity#ERROR}.
     *
     * @param message the message.
     */
    void error(Object message);
}
