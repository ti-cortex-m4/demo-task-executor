package demo.logger

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Abstract logger that allows to add  prefix to messages.
 * This class IS NOT thread-safe.
 */
abstract class AbstractLogger extends Logger {

    val PATTERN = "hh:mm:ss.SSS"
    val FORMATTER = DateTimeFormatter.ofPattern(PATTERN)

    protected def getMessagePrefix(severity: Severity): String = {
        val sb = new StringBuilder()
        sb.append(LocalDateTime.now().format(FORMATTER))
        sb.append(" ")
        sb.append(severity)
        sb.append(" [")
        sb.append(Thread.currentThread().getName)
        sb.append("] ")
        sb.toString()
    }

    /**
     * {@inheritDoc}
     */
    override def trace(message: Any) {
        log(TRACE, message)
    }

    /**
     * {@inheritDoc}
     */
    override def debug(message: Any) {
        log(DEBUG, message)
    }

    /**
     * {@inheritDoc}
     */
    override def info(message: Any) {
        log(INFO, message)
    }

    /**
     * {@inheritDoc}
     */
    override def warn(message: Any) {
        log(WARN, message)
    }

    /**
     * {@inheritDoc}
     */
    override def error(message: Any) {
        log(ERROR, message)
    }
}
