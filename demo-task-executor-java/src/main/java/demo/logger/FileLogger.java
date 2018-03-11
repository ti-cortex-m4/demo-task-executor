package demo.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * Logger that writes messages to a file.
 * This class IS thread-safe.
 */
public class FileLogger extends AbstractLogger {

    private final File file;

    /**
     * Creates a file logger and deletes its previous content.
     *
     * @param fileName the file logger name.
     */
    public FileLogger(String fileName) {
        this.file = new File(Objects.requireNonNull(fileName));
        file.delete();
    }

    @Override
    synchronized public void log(Severity severity, Object message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(getMessagePrefix(severity) + message + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
