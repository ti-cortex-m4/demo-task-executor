package demo.executor

import org.testng.Assert.assertTrue

/**
 * Abstract unit test for testing concurrency.
 */
open class AbstractConcurrencyTest {

    private val RELATIVE_ERROR_PERCENT = 5

    protected fun assertAlmostEquals(actual: Long, expected: Long) {
        assertTrue(Math.abs(100 * (actual - expected) / expected) < RELATIVE_ERROR_PERCENT)
    }
}
