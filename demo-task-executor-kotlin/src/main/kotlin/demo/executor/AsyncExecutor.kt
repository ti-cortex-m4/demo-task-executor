package demo.executor

import demo.logger.ConsoleLogger
import demo.logger.FileLogger
import demo.logger.LoggerFactory
import demo.logger.Severity
import java.time.Duration
import java.time.LocalDateTime
import java.util.Optional
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicInteger

/**
 * Executor to run multiple [demo.executor.Task] asynchronously.
 */
class AsyncExecutor
/**
 * Creates an executor with given threads count.

 * @param threadsCount threads count for the executor.
 */
(threadsCount: Int) : Executor {

    private val tasks: BlockingQueue<Task<*>> = LinkedBlockingQueue<Task<*>>()
    private val submittedTasks = AtomicInteger(0)

    @Volatile private var running = true
    private val start = LocalDateTime.now()

    init {
        require(threadsCount > 0, { "Threads count must be greater than 0" })
        val runnables = arrayOfNulls<Thread>(threadsCount)

        for (i in 0..threadsCount - 1) {
            val thread = Thread {
                while (running) {
                    while (!tasks.isEmpty()) {
                        var task = Optional.empty<Task<*>>()
                        try {
                            task = Optional.of(tasks.take())

                            LOGGER.debug("start $task")
                            val result = task.get().getCallable().call()
                            LOGGER.debug("stop $task with $result")

                            task.get().setResult(result!!)
                            submittedTasks.decrementAndGet()
                        } catch (e: Exception) {
                            task.ifPresent { t ->
                                t.setException(e)
                                submittedTasks.decrementAndGet()
                            }
                        }

                    }
                }
            }

            thread.start()
            runnables[i] = thread
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun submit(task: Task<*>) {
        LOGGER.debug("submit $task")
        tasks.add(task)
        submittedTasks.incrementAndGet()
    }

    /**
     * {@inheritDoc}
     */
    override fun waitUntilFinished() {
        while (submittedTasks.get() != 0) {
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun stop(): Duration {
        running = false
        return Duration.between(start, LocalDateTime.now())
    }

    companion object {

        private val LOGGER = LoggerFactory(AsyncExecutor::class)
                .add(FileLogger("demo-task-executor-kotlin.log"), Severity.DEBUG)
                .add(ConsoleLogger(), Severity.TRACE)
                .build()
    }
}
