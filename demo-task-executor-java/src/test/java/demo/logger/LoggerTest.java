package demo.logger;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for {@link demo.logger.Logger} implementations.
 */
@Test
public class LoggerTest {

    private static final String LOG_PREFIX = "build" + File.separator;

    private static final String TRACE_LOG = LOG_PREFIX + "trace.log";
    private static final String DEBUG_LOG = LOG_PREFIX + "debug.log";
    private static final String INFO_LOG = LOG_PREFIX + "info.log";
    private static final String WARN_LOG = LOG_PREFIX + "warn.log";
    private static final String ERROR_LOG = LOG_PREFIX + "error.log";

    @Test
    public void testSeverity() throws IOException {
        Logger logger = new LoggerFactory(LoggerTest.class)
                .add(new FileLogger(TRACE_LOG), Severity.TRACE)
                .add(new FileLogger(DEBUG_LOG), Severity.DEBUG)
                .add(new FileLogger(INFO_LOG), Severity.INFO)
                .add(new FileLogger(WARN_LOG), Severity.WARN)
                .add(new FileLogger(ERROR_LOG), Severity.ERROR)
                .build();

        logger.log(Severity.TRACE, "trace");
        logger.log(Severity.DEBUG, "debug");
        logger.log(Severity.INFO, "info");
        logger.log(Severity.WARN, "warn");
        logger.log(Severity.ERROR, "error");

        List<String> lines = Files.readAllLines(Paths.get(TRACE_LOG));
        assertEquals(lines.size(), 5);
        assertTrue(lines.get(0).endsWith("trace"));
        assertTrue(lines.get(1).endsWith("debug"));
        assertTrue(lines.get(2).endsWith("info"));
        assertTrue(lines.get(3).endsWith("warn"));
        assertTrue(lines.get(4).endsWith("error"));

        lines = Files.readAllLines(Paths.get(DEBUG_LOG));
        assertEquals(lines.size(), 4);
        assertTrue(lines.get(0).endsWith("debug"));
        assertTrue(lines.get(1).endsWith("info"));
        assertTrue(lines.get(2).endsWith("warn"));
        assertTrue(lines.get(3).endsWith("error"));

        lines = Files.readAllLines(Paths.get(INFO_LOG));
        assertEquals(lines.size(), 3);
        assertTrue(lines.get(0).endsWith("info"));
        assertTrue(lines.get(1).endsWith("warn"));
        assertTrue(lines.get(2).endsWith("error"));

        lines = Files.readAllLines(Paths.get(WARN_LOG));
        assertEquals(lines.size(), 2);
        assertTrue(lines.get(0).endsWith("warn"));
        assertTrue(lines.get(1).endsWith("error"));

        lines = Files.readAllLines(Paths.get(ERROR_LOG));
        assertEquals(lines.size(), 1);
        assertTrue(lines.get(0).endsWith("error"));
    }
}
