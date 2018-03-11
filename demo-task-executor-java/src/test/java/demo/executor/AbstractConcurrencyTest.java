package demo.executor;

import static org.testng.Assert.assertTrue;

/**
 * Abstract unit test for testing concurrency.
 */
public class AbstractConcurrencyTest {

    private static final int RELATIVE_ERROR_PERCENT = 5;

    protected void assertAlmostEquals(long actual, long expected) {
        assertTrue(Math.abs(100 * (actual - expected) / expected) < RELATIVE_ERROR_PERCENT);
    }
}
