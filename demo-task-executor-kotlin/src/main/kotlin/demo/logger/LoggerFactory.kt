package demo.logger

import java.util.HashMap
import kotlin.reflect.KClass

/**
 * Factory that builds a logger facade that writes messages to many [demo.logger.Logger] simultaneously.
 * This class IS thread-safe.
 */
class LoggerFactory
/**
 * Creates the logging factory for a client class.

 * @param clazz the client clazz.
 */
(clazz: KClass<*>) {

    private val className: String
    private val loggers: MutableMap<Logger, Severity>

    init {
        this.className = clazz.simpleName!!
        this.loggers = HashMap<Logger, Severity>()
    }

    /**
     * Add a logger to the logging factory.

     * @param logger   the logger.
     * *
     * @param severity the logging severity for the logger.
     */
    @Synchronized fun add(logger: Logger, severity: Severity): LoggerFactory {
        loggers.put(requireNotNull(logger), requireNotNull(severity))
        return this
    }

    /**
     * Builds a thread-safe logger facade from the added loggers.

     * @return the thread-safe logger facade.
     */
    fun build(): Logger {
        return object : AbstractLogger() {
            @Synchronized override fun log(severity: Severity, message: Any) {
                for (logger in loggers.keys) {
                    if (severity.value >= loggers[logger]!!.value) {
                        logger.log(severity, className + " - " + message)
                    }
                }
            }
        }
    }
}
