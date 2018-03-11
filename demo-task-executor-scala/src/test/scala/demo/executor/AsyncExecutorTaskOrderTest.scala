package demo.executor

import java.util

import org.testng.Assert.assertEquals
import org.testng.Assert.fail
import org.testng.annotations.Test
import java.util.ArrayList

/**
 * Unit test for {[demo.executor.AsyncExecutor]} to test run order of [demo.executor.Task].
 */
@Test
class AsyncExecutorTaskOrderTest extends AbstractAsyncExecutorTest {

    @Test
    def testTaskOrder() {
        val TASKS_COUNT = 8

        val consumer = new ArrayList[Integer]()
        val executor = new AsyncExecutor(1)

        for (i <- 0  until TASKS_COUNT) {
            val task = getTestTask(consumer, i)
            executor.submit(task)
        }

        executor.waitUntilFinished()
        executor.stop()

        assertEquals(consumer.size, TASKS_COUNT)
        for (i <- 0 until TASKS_COUNT) {
            assertEquals(consumer.get(i), i)
        }
    }
}
