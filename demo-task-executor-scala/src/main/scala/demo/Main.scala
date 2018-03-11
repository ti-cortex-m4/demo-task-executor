package demo

import java.time.{Duration, LocalDateTime}
import java.util.concurrent.TimeUnit

import demo.CallableHelper._
import demo.executor.{AsyncExecutor, Task}
import demo.logger._

object Main {

    private val LOGGER = new LoggerFactory(Main.getClass)
        .add(new FileLogger("demo-task-executor-scala.log"), DEBUG)
        .add(new ConsoleLogger(), TRACE)
        .build()

    private val THREADS_COUNT = 2

    def main(args: Array[String]) = {
        LOGGER.trace("Start application")
        val start = LocalDateTime.now()

        val executor = new AsyncExecutor(THREADS_COUNT)

        val task1 = new Task[String]("task1", classOf[String],
            callable {
                () => {
                    TimeUnit.SECONDS.sleep(3)
                    "OK"
                }
            }, { (result, exception) => accept(result, exception) })

        val task2 = new Task[Integer]("task2", classOf[Integer],
            callable {
                () => {
                    TimeUnit.SECONDS.sleep(1)
                    0
                }
            }, { (result, exception) => accept(result, exception) })

        val task3 = new Task[Float]("task3", classOf[Float],
            callable {
                () => {
                    TimeUnit.SECONDS.sleep(1)
                    throw new RuntimeException()
                }
            }, { (result, exception) => accept(result, exception) })

        executor.submit(task1)
        executor.submit(task2)
        executor.submit(task3)

        executor.waitUntilFinished()
        executor.stop()

        LOGGER.trace("Stop application in " + Duration.between(start, LocalDateTime.now()).getSeconds + " seconds")
    }

    private def accept[T](result: Option[T], exception: Option[Exception]) {
        if (exception.nonEmpty) {
            LOGGER.debug("exception: " + exception)
        } else {
            LOGGER.debug("result: " + result)
        }
    }
}
