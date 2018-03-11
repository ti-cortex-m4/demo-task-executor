package demo.logger

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * Logger that writes messages to a file.
 * This class IS thread-safe.
 */
class FileLogger
/**
 * Creates a file logger and deletes its previous content.

 * @param fileName the file logger name.
 */
(fileName: String) : AbstractLogger() {

    private val file: File

    init {
        this.file = File(requireNotNull(fileName))
        file.delete()
    }

    @Synchronized override fun log(severity: Severity, message: Any) {
        BufferedWriter(FileWriter(file, true)).use { writer ->
            writer.write(getMessagePrefix(severity) + message + "\n")
            writer.flush()
        }
    }
}
