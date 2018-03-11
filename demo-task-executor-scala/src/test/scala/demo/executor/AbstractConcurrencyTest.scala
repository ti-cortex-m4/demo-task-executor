package demo.executor

import org.testng.Assert.assertTrue

/**
 * Abstract unit test for testing concurrency.
 */
abstract class AbstractConcurrencyTest {

    private val RELATIVE_ERROR_PERCENT = 5

    protected def assertAlmostEquals(actual: Long, expected: Long) {
        assertTrue(Math.abs(100 * (actual - expected) / expected) < RELATIVE_ERROR_PERCENT)
    }
}
