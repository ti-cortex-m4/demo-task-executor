package demo.logger

/**
  * Logger that writes messages to the console ("standard" output stream).
  * This class IS thread-safe.
  */
class ConsoleLogger extends AbstractLogger {

    override def log(severity: Severity, message: Any) {
        this.synchronized {
            println(getMessagePrefix(severity) + message)
        }
    }
}
