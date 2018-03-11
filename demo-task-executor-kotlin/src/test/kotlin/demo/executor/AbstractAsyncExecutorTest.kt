package demo.executor

import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.Assert.fail
import java.util.Optional
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * Abstract unit test for {[demo.executor.AsyncExecutor]}.
 */
abstract class AbstractAsyncExecutorTest : AbstractConcurrencyTest() {

    protected val TASK_DURATION_MSEC: Long = 1000

    protected fun getTestTask(value: Int, action: (Optional<Int>, Optional<Exception>) -> Unit): Task<Int> {
        return getTestTask(value, Int::class, action)
    }

    protected fun getTestTaskWithResult(value: Int): Task<Int> {
        return getTestTaskWithResult(value, Int::class)
    }

    protected fun getTestTaskWithException(value: Int): Task<Int> {
        return getTestTaskWithException(value, Int::class)
    }

    protected fun getTestTask(consumer: MutableList<Int>, value: Int): Task<Int> {
        return getTestTask(value)
        { result, exception ->
            if (exception.isPresent) {
                fail()
            } else {
                consumer.add(value)
            }
        }
    }

    private fun <T : Any> getTestTask(value: T, clazz: KClass<T>, action: (Optional<T>, Optional<Exception>) -> Unit): Task<T> {
        return Task("task " + value, clazz,
                Callable {
                    TimeUnit.MILLISECONDS.sleep(TASK_DURATION_MSEC)
                    value
                },
                action)
    }

    private fun <T : Any> getTestTaskWithResult(value: T, clazz: KClass<T>): Task<T> {
        return Task("task " + value, clazz,
                Callable {
                    TimeUnit.MILLISECONDS.sleep(TASK_DURATION_MSEC)
                    value
                }
        ) { result, exception ->
            if (exception.isPresent) {
                fail()
            } else {
                assertEquals(result.get(), value)
            }
        }
    }

    private fun <T : Any> getTestTaskWithException(value: T, clazz: KClass<T>): Task<T> {
        return Task<T>("task " + value, clazz,
                Callable {
                    TimeUnit.MILLISECONDS.sleep(TASK_DURATION_MSEC)
                    throw IllegalArgumentException()
                }
        ) { result, exception ->
            if (exception.isPresent) {
                assertTrue(exception.get() is IllegalArgumentException)
            } else {
                fail()
            }
        }
    }
}
