package demo.logger;

/**
 * Logger that writes messages to the console ("standard" output stream).
 * This class IS thread-safe.
 */
public class ConsoleLogger extends AbstractLogger {

    @Override
    synchronized public void log(Severity severity, Object message) {
        System.out.println(getMessagePrefix(severity) + message);
    }
}
