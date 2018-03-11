package demo.executor

import org.testng.annotations.Test

/**
 * Unit test for {[demo.executor.AsyncExecutor]} to test results and exceptions of [demo.executor.Task].
 */
@Test
class AsyncExecutorResultExceptionTest : AbstractAsyncExecutorTest() {

    @Test
    fun testResult() {
        val executor = AsyncExecutor(1)

        val task1 = getTestTaskWithResult(1)
        executor.submit(task1)

        executor.waitUntilFinished()
        executor.stop()
    }

    @Test
    fun testResultResult() {
        val executor = AsyncExecutor(1)

        val task1 = getTestTaskWithResult(11)
        executor.submit(task1)
        val task2 = getTestTaskWithResult(12)
        executor.submit(task2)

        executor.waitUntilFinished()
        executor.stop()
    }

    @Test
    fun testException() {
        val executor = AsyncExecutor(1)

        val task1 = getTestTaskWithException(2)
        executor.submit(task1)

        task1.waitUntilFinished()
        executor.stop()
    }

    @Test
    fun testExceptionException() {
        val executor = AsyncExecutor(1)

        val task1 = getTestTaskWithException(21)
        executor.submit(task1)
        val task2 = getTestTaskWithException(22)
        executor.submit(task2)

        executor.waitUntilFinished()
        executor.stop()
    }

    @Test
    fun testResultException() {
        val executor = AsyncExecutor(1)

        val task1 = getTestTaskWithResult(31)
        executor.submit(task1)
        val task2 = getTestTaskWithException(32)
        executor.submit(task2)

        executor.waitUntilFinished()
        executor.stop()
    }

    @Test
    fun testExceptionResult() {
        val executor = AsyncExecutor(1)

        val task1 = getTestTaskWithException(41)
        executor.submit(task1)
        val task2 = getTestTaskWithResult(42)
        executor.submit(task2)

        executor.waitUntilFinished()
        executor.stop()
    }
}
