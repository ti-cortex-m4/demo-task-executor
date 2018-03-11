package demo

import demo.executor.AsyncExecutor
import demo.executor.Task
import demo.logger.ConsoleLogger
import demo.logger.FileLogger
import demo.logger.LoggerFactory
import demo.logger.Severity
import java.time.Duration
import java.time.LocalDateTime
import java.util.Optional
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

object Main {

    private val LOGGER = LoggerFactory(Main::class)
            .add(FileLogger("demo-task-executor-kotlin.log"), Severity.DEBUG)
            .add(ConsoleLogger(), Severity.TRACE)
            .build()

    private const val THREADS_COUNT = 2

    @JvmStatic fun main(args: Array<String>) {
        LOGGER.trace("Start application")
        val start = LocalDateTime.now()

        val executor = AsyncExecutor(THREADS_COUNT)

        val task1 = Task<String>("task1", String::class,
                Callable {
                    TimeUnit.SECONDS.sleep(3)
                    "OK"
                },
                { result, exception -> accept(result, exception) })

        val task2 = Task<Int>("task2", Int::class,
                Callable {
                    TimeUnit.SECONDS.sleep(1)
                    0
                },
                { result, exception -> accept(result, exception) })

        val task3 = Task<Float>("task3", Float::class,
                Callable {
                    TimeUnit.SECONDS.sleep(1)
                    throw RuntimeException()
                },
                { result, exception -> accept(result, exception) })

        executor.submit(task1)
        executor.submit(task2)
        executor.submit(task3)

        executor.waitUntilFinished()
        executor.stop()

        LOGGER.trace("Stop application in " + Duration.between(start, LocalDateTime.now()).seconds + " seconds")
    }

    private fun <T> accept(result: Optional<T>, exception: Optional<Exception>) {
        if (exception.isPresent) {
            LOGGER.debug("exception: " + exception)
        } else {
            LOGGER.debug("result: " + result)
        }
    }
}
