package demo.logger

/**
 * Logger that writes messages to the console ("standard" output stream).
 * This class IS thread-safe.
 */
class ConsoleLogger : AbstractLogger() {

    @Synchronized override fun log(severity: Severity, message: Any) {
        println(getMessagePrefix(severity) + message)
    }
}
