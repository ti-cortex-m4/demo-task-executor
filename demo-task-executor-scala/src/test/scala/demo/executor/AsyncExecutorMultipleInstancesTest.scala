package demo.executor

import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import java.util.ArrayList

/**
 * Unit test for {[demo.executor.AsyncExecutor]} to test multiple instances.
 */
@Test
class AsyncExecutorMultipleInstancesTest extends AbstractAsyncExecutorTest {

    @Test
    def testMultipleInstances() {
        val TASKS_COUNT = 4 * 2

        val consumer1 = new java.util.ArrayList[Integer]()
        val consumer2 = new java.util.ArrayList[Integer]()

        val executor1 = new AsyncExecutor(1)
        val executor2 = new AsyncExecutor(1)

        for (i <- 0 until TASKS_COUNT) {
            if (i % 2 == 0) {
                val task1 = getTestTask(consumer1, i)
                executor1.submit(task1)
            } else {
                val task2 = getTestTask(consumer2, i)
                executor2.submit(task2)
            }
        }

        executor1.waitUntilFinished()
        executor1.stop()

        executor2.waitUntilFinished()
        executor2.stop()

        assertEquals(consumer1.size, TASKS_COUNT / 2)
        assertEquals(consumer2.size, TASKS_COUNT / 2)

        for (i <- 0 until TASKS_COUNT / 2) {
            if (i % 2 == 0) {
                assertEquals(consumer1.get(i), i * 2)
            } else {
                assertEquals(consumer2.get(i), i * 2 + 1)
            }
        }
    }
}
