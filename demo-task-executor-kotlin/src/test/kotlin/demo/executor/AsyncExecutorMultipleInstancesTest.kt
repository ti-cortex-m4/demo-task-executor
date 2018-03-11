package demo.executor

import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import java.util.ArrayList

/**
 * Unit test for {[demo.executor.AsyncExecutor]} to test multiple instances.
 */
@Test
class AsyncExecutorMultipleInstancesTest : AbstractAsyncExecutorTest() {

    @Test
    fun testMultipleInstances() {
        val TASKS_COUNT = 4 * 2

        val consumer1 = ArrayList<Int>()
        val consumer2 = ArrayList<Int>()

        val executor1 = AsyncExecutor(1)
        val executor2 = AsyncExecutor(1)

        for (i in 0..TASKS_COUNT - 1) {
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

        for (i in 0..TASKS_COUNT / 2 - 1) {
            if (i % 2 == 0) {
                assertEquals(consumer1[i], i * 2)
            } else {
                assertEquals(consumer2[i], i * 2 + 1)
            }
        }
    }
}
