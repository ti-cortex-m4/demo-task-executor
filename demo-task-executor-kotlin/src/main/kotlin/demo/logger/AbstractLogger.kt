package demo.logger

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Abstract logger that allows to add  prefix to messages.
 * This class IS NOT thread-safe.
 */
abstract class AbstractLogger : Logger {

    protected fun getMessagePrefix(severity: Severity): String {
        val sb = StringBuilder()
        sb.append(LocalDateTime.now().format(FORMATTER))
        sb.append(" ")
        sb.append(severity)
        sb.append(" [")
        sb.append(Thread.currentThread().name)
        sb.append("] ")
        return sb.toString()
    }

    /**
     * {@inheritDoc}
     */
    override fun trace(message: Any) {
        log(Severity.TRACE, message)
    }

    /**
     * {@inheritDoc}
     */
    override fun debug(message: Any) {
        log(Severity.DEBUG, message)
    }

    /**
     * {@inheritDoc}
     */
    override fun info(message: Any) {
        log(Severity.INFO, message)
    }

    /**
     * {@inheritDoc}
     */
    override fun warn(message: Any) {
        log(Severity.WARN, message)
    }

    /**
     * {@inheritDoc}
     */
    override fun error(message: Any) {
        log(Severity.ERROR, message)
    }

    companion object {

        private const val PATTERN = "hh:mm:ss.SSS"
        private val FORMATTER = DateTimeFormatter.ofPattern(PATTERN)
    }
}
