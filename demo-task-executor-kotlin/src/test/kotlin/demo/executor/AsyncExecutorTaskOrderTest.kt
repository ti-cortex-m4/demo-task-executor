package demo.executor

import org.testng.Assert.assertEquals
import org.testng.Assert.fail
import org.testng.annotations.Test
import java.util.ArrayList

/**
 * Unit test for {[demo.executor.AsyncExecutor]} to test run order of [demo.executor.Task].
 */
@Test
class AsyncExecutorTaskOrderTest : AbstractAsyncExecutorTest() {

    @Test
    fun testTaskOrder() {
        val TASKS_COUNT = 8

        val consumer = ArrayList<Int>()
        val executor = AsyncExecutor(1)

        for (i in 0..TASKS_COUNT - 1) {
            val task = getTestTask(consumer, i)
            executor.submit(task)
        }

        executor.waitUntilFinished()
        executor.stop()

        assertEquals(consumer.size, TASKS_COUNT)
        for (i in 0..TASKS_COUNT - 1) {
            assertEquals(consumer[i], i)
        }
    }
}
