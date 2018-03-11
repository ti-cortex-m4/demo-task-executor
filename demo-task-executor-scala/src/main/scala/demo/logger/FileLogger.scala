package demo.logger

import java.io.{BufferedWriter, File, FileWriter}
import java.util.Objects

import demo.AutoCloseableHelper._

/**
  * Logger that writes messages to a file.
  * This class IS thread-safe.
  */
class FileLogger(fileName: String) extends AbstractLogger {

    private val file = new File(Objects.requireNonNull(fileName))
    file.delete()

    override def log(severity: Severity, message: Any) {
        this.synchronized {
            use(new BufferedWriter(new FileWriter(file, true))) {
                writer =>
                    writer.write(getMessagePrefix(severity) + message + "\n")
                    writer.flush()
            }
        }
    }
}
