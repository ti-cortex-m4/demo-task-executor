package demo.logger

import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Unit test for [demo.logger.Logger] implementations.
 */
@Test
class LoggerTest {

    private val LOG_PREFIX = "build" + File.separator

    private val TRACE_LOG = LOG_PREFIX + "trace.log"
    private val DEBUG_LOG = LOG_PREFIX + "debug.log"
    private val INFO_LOG = LOG_PREFIX + "info.log"
    private val WARN_LOG = LOG_PREFIX + "warn.log"
    private val ERROR_LOG = LOG_PREFIX + "error.log"

    @Test
    fun testSeverity() {
        val logger = LoggerFactory(LoggerTest::class)
                .add(FileLogger(TRACE_LOG), Severity.TRACE)
                .add(FileLogger(DEBUG_LOG), Severity.DEBUG)
                .add(FileLogger(INFO_LOG), Severity.INFO)
                .add(FileLogger(WARN_LOG), Severity.WARN)
                .add(FileLogger(ERROR_LOG), Severity.ERROR)
                .build()

        logger.log(Severity.TRACE, "trace")
        logger.log(Severity.DEBUG, "debug")
        logger.log(Severity.INFO, "info")
        logger.log(Severity.WARN, "warn")
        logger.log(Severity.ERROR, "error")

        var lines = Files.readAllLines(Paths.get(TRACE_LOG))
        assertEquals(lines.size, 5)
        assertTrue(lines[0].endsWith("trace"))
        assertTrue(lines[1].endsWith("debug"))
        assertTrue(lines[2].endsWith("info"))
        assertTrue(lines[3].endsWith("warn"))
        assertTrue(lines[4].endsWith("error"))

        lines = Files.readAllLines(Paths.get(DEBUG_LOG))
        assertEquals(lines.size, 4)
        assertTrue(lines[0].endsWith("debug"))
        assertTrue(lines[1].endsWith("info"))
        assertTrue(lines[2].endsWith("warn"))
        assertTrue(lines[3].endsWith("error"))

        lines = Files.readAllLines(Paths.get(INFO_LOG))
        assertEquals(lines.size, 3)
        assertTrue(lines[0].endsWith("info"))
        assertTrue(lines[1].endsWith("warn"))
        assertTrue(lines[2].endsWith("error"))

        lines = Files.readAllLines(Paths.get(WARN_LOG))
        assertEquals(lines.size, 2)
        assertTrue(lines[0].endsWith("warn"))
        assertTrue(lines[1].endsWith("error"))

        lines = Files.readAllLines(Paths.get(ERROR_LOG))
        assertEquals(lines.size, 1)
        assertTrue(lines[0].endsWith("error"))
    }
}
