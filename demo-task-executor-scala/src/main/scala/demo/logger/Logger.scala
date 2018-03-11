package demo.logger

/**
 * Logger that allows to write messages with different severity.
 * Its implementations MUST be thread-safe.
 */
trait Logger {

    /**
     * Writes a message with the given severity.

     * @param severity the message severity.
     * @param message  the message.
     */
    def log(severity: Severity, message: Any)

    /**
     * Writes a message with the severity [demo.logger.Severity.TRACE].

     * @param message the message.
     */
    def trace(message: Any)

    /**
     * Writes a message with the severity [demo.logger.Severity.DEBUG].

     * @param message the message.
     */
    def debug(message: Any)

    /**
     * Writes a message with the severity [demo.logger.Severity.INFO].

     * @param message the message.
     */
    def info(message: Any)

    /**
     * Writes a message with the severity [demo.logger.Severity.WARN].

     * @param message the message.
     */
    def warn(message: Any)

    /**
     * Writes a message with the severity [demo.logger.Severity.ERROR].

     * @param message the message.
     */
    def error(message: Any)
}
