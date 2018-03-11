package demo.executor

import org.testng.annotations.Test

import java.time.Duration
import java.time.LocalDateTime

/**
 * Unit test for {[demo.executor.AsyncExecutor]} to test concurrency.
 */
@Test
class AsyncExecutorConcurrencyTest extends AbstractAsyncExecutorTest {

    @Test
    def testConcurrencyDuration() {
        assertAlmostEquals(testConcurrencyDuration(1), TASK_DURATION_MSEC * 4)
        assertAlmostEquals(testConcurrencyDuration(2), TASK_DURATION_MSEC * 2)
        assertAlmostEquals(testConcurrencyDuration(4), TASK_DURATION_MSEC)
    }

    private def testConcurrencyDuration(threadsCount: Integer): Long = {
        val TASKS_COUNT = 4

        val executor = new AsyncExecutor(threadsCount)
        val start = LocalDateTime.now()

        for (i <- 0 until TASKS_COUNT) {
            val task = getTestTaskWithResult(i)
            executor.submit(task)
        }

        executor.waitUntilFinished()
        executor.stop()

        val duration = Duration.between(start, LocalDateTime.now())
        duration.getSeconds * 1000
    }
}
