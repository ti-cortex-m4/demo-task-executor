package demo.executor

import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.Assert.fail
import org.testng.annotations.Test
import java.util.HashSet
import java.util.concurrent.Callable

/**
 * Unit tests for [demo.executor.Task].
 */
@Test
class TaskTest : AbstractConcurrencyTest() {

    @Test
    fun testSetResult() {
        val consumer = HashSet<String>()
        val task = getTestTaskWithResult(consumer)
        assertEquals(task.isFinished(), false)

        task.setResult(RESULT)

        assertEquals(task.isFinished(), true)
        assertEquals(consumer.size, 1)
        assertTrue(consumer.contains(RESULT))
    }

    @Test
    fun testSetException() {
        val consumer = HashSet<Exception>()
        val task = getTaskWithException(consumer)
        assertEquals(task.isFinished(), false)

        task.setException(EXCEPTION)

        assertEquals(task.isFinished(), true)
        assertEquals(consumer.size, 1)
        assertTrue(consumer.contains(EXCEPTION))
    }

    @Test
    fun testGetCallableWithResult() {
        val task = getTestTaskWithResult(HashSet<String>())
        assertEquals(task.getCallable().call(), RESULT)
    }

    @Test(expectedExceptions = arrayOf(RuntimeException::class))
    fun testGetCallableWithException() {
        val task = getTaskWithException(HashSet<Exception>())
        task.getCallable().call()
    }

    @Test(expectedExceptions = arrayOf(IllegalStateException::class))
    fun testMarkFinishedException() {
        val task = getTestTaskWithResult(HashSet<String>())
        task.setResult(RESULT)
        task.markFinished()
    }

    private fun getTestTaskWithResult(consumer: MutableSet<String>): Task<String> {
        return Task("task with result", String::class,
                Callable { RESULT }
        ) { result, exception ->
            if (exception.isPresent) {
                fail()
            } else {
                consumer.add(result.get())
            }
        }
    }

    private fun getTaskWithException(consumer: MutableSet<Exception>): Task<String> {
        return Task("task with exception", String::class,
                Callable<String> { throw EXCEPTION }
        ) { result, exception ->
            if (exception.isPresent) {
                consumer.add(exception.get())
            } else {
                fail()
            }
        }
    }

    companion object {

        private const val RESULT = "result"
        private val EXCEPTION = RuntimeException()
    }
}
