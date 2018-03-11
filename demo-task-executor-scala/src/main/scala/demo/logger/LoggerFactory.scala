package demo.logger

import java.util.Objects

/**
  * Factory that builds a logger facade that writes messages to many [demo.logger.Logger] simultaneously.
  * This class IS thread-safe.
  */
class LoggerFactory(clazz: Class[_]) {

    val className = clazz.getPackage.getName + "." + clazz.getSimpleName.split("\\$").last
    val loggers = new collection.mutable.HashMap[Logger, Severity]()

    /**
      * Add a logger to the logging factory.
      *
      * @param logger   the logger.
      * @param severity the logging severity for the logger.
      */
    def add(logger: Logger, severity: Severity): LoggerFactory = {
        this.synchronized {
            loggers.put(Objects.requireNonNull(logger), Objects.requireNonNull(severity))
            return this
        }
    }

    /**
      * Builds a thread-safe logger facade from the added loggers.
      *
      * @return the thread-safe logger facade.
      */
    def build(): Logger = new AbstractLogger {
        override def log(severity: Severity, message: Any) {
            this.synchronized {
                for (logger <- loggers.keySet) {
                    if (SeverityConverter.toCode(severity) >= SeverityConverter.toCode(loggers.get(logger).get)) {
                        logger.log(severity, className + " - " + message)
                    }
                }
            }
        }
    }
}
