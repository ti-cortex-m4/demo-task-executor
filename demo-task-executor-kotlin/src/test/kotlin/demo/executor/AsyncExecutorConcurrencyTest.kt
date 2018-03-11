package demo.executor

import org.testng.annotations.Test

import java.time.Duration
import java.time.LocalDateTime

/**
 * Unit test for {[demo.executor.AsyncExecutor]} to test concurrency.
 */
@Test
class AsyncExecutorConcurrencyTest : AbstractAsyncExecutorTest() {

    @Test
    fun testConcurrencyDuration() {
        assertAlmostEquals(testConcurrencyDuration(1), TASK_DURATION_MSEC * 4)
        assertAlmostEquals(testConcurrencyDuration(2), TASK_DURATION_MSEC * 2)
        assertAlmostEquals(testConcurrencyDuration(4), TASK_DURATION_MSEC)
    }

    private fun testConcurrencyDuration(threadsCount: Int): Long {
        val TASKS_COUNT = 4

        val executor = AsyncExecutor(threadsCount)
        val start = LocalDateTime.now()

        for (i in 0..TASKS_COUNT - 1) {
            val task = getTestTaskWithResult(i)
            executor.submit(task)
        }

        executor.waitUntilFinished()
        executor.stop()

        val duration = Duration.between(start, LocalDateTime.now())
        return duration.seconds * 1000
    }
}
