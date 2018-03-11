package demo.executor

import java.time.Duration
import java.time.LocalDateTime
import java.util.Optional
import java.util.concurrent.Callable
import kotlin.reflect.KClass

/**
 * Task that combines [java.util.concurrent.Callable] as producer and [demo.executor.Action] as consumer.
 * This class IS thread-safe.
 */
class Task<T : Any>
/**
 * Creates an task.

 * @param name     the task name.
 * @param clazz    the result class type token to cast generic type at runtime.
 * @param callable the [java.util.concurrent.Callable] to run.
 * @param action   the action to process result or exception.
 */
(name: String, clazz: KClass<T>, callable: Callable<T>, action: (Optional<T>, Optional<Exception>) -> Unit) {

    private val name: String
    private val clazz: KClass<T>
    private val callable: Callable<T>
    private val action: (Optional<T>, Optional<Exception>) -> Unit

    @Volatile internal var finished = false
    private val start = LocalDateTime.now()
    private var duration = Optional.empty<Duration>()

    init {
        this.name = requireNotNull(name)
        this.clazz = requireNotNull(clazz)
        this.callable = requireNotNull(callable)
        this.action = requireNotNull(action)
    }

    internal fun getCallable(): Callable<T> {
        return this.callable
    }

    internal fun isFinished(): Boolean {
        return finished
    }

    internal fun markFinished() {
        check (!finished, { "Task must be non-finished" })
        duration = Optional.of(Duration.between(start, LocalDateTime.now()))
        finished = true
    }

    @Synchronized internal fun setResult(result: Any) {
        val result2 = if (result is Number) clazz.javaObjectType.cast(result) else clazz.java.cast(result)
        this.action(Optional.of(result2), Optional.empty<Exception>())
        markFinished()
    }

    @Synchronized internal fun setException(exception: Exception) {
        this.action(Optional.empty<T>(), Optional.of(requireNotNull(exception)))
        markFinished()
    }

    /**
     * Waits until the task is finished.

     * @return the task duration.
     */
    fun waitUntilFinished(): Duration {
        while (!finished) {
        }
        require (duration.isPresent, { "Duration must be set" })
        return duration.get()
    }

    override fun toString(): String {
        return "Task{'$name'}"
    }
}
