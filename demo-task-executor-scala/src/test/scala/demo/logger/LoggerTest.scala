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

    private val LOG_PREFIX = "target" + File.separator

    private val TRACE_LOG = LOG_PREFIX + "trace.log"
    private val DEBUG_LOG = LOG_PREFIX + "debug.log"
    private val INFO_LOG = LOG_PREFIX + "info.log"
    private val WARN_LOG = LOG_PREFIX + "warn.log"
    private val ERROR_LOG = LOG_PREFIX + "error.log"

    val logger = new LoggerFactory(this.getClass)
        .add(new FileLogger(TRACE_LOG), TRACE)
        .add(new FileLogger(DEBUG_LOG), DEBUG)
        .add(new FileLogger(INFO_LOG), INFO)
        .add(new FileLogger(WARN_LOG), WARN)
        .add(new FileLogger(ERROR_LOG), ERROR)
        .build()

    @Test
    def testSeverity() {
        logger.log(TRACE, "trace")
        logger.log(DEBUG, "debug")
        logger.log(INFO, "info")
        logger.log(WARN, "warn")
        logger.log(ERROR, "error")

        var lines = Files.readAllLines(Paths.get(TRACE_LOG))
        assertEquals(lines.size, 5)
        assertTrue(lines.get(0).endsWith("trace"))
        assertTrue(lines.get(1).endsWith("debug"))
        assertTrue(lines.get(2).endsWith("info"))
        assertTrue(lines.get(3).endsWith("warn"))
        assertTrue(lines.get(4).endsWith("error"))

        lines = Files.readAllLines(Paths.get(DEBUG_LOG))
        assertEquals(lines.size, 4)
        assertTrue(lines.get(0).endsWith("debug"))
        assertTrue(lines.get(1).endsWith("info"))
        assertTrue(lines.get(2).endsWith("warn"))
        assertTrue(lines.get(3).endsWith("error"))

        lines = Files.readAllLines(Paths.get(INFO_LOG))
        assertEquals(lines.size, 3)
        assertTrue(lines.get(0).endsWith("info"))
        assertTrue(lines.get(1).endsWith("warn"))
        assertTrue(lines.get(2).endsWith("error"))

        lines = Files.readAllLines(Paths.get(WARN_LOG))
        assertEquals(lines.size, 2)
        assertTrue(lines.get(0).endsWith("warn"))
        assertTrue(lines.get(1).endsWith("error"))

        lines = Files.readAllLines(Paths.get(ERROR_LOG))
        assertEquals(lines.size, 1)
        assertTrue(lines.get(0).endsWith("error"))
    }
}
