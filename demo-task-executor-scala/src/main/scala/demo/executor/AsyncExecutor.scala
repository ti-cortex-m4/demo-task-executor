package demo.executor

import java.time.{Duration, LocalDateTime}
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import demo.logger._

/**
  * Executor to run multiple [demo.executor.Task] asynchronously.
  */
final class AsyncExecutor(threadsCount: Integer) extends Executor {

    val LOGGER = new LoggerFactory(this.getClass)
        .add(new FileLogger("demo-task-executor-scala.log"), DEBUG)
        .add(new ConsoleLogger(), TRACE)
        .build()

    private val tasks: BlockingQueue[Task[_]] = new LinkedBlockingQueue[Task[_]]()
    private val submittedTasks = new AtomicInteger(0)

    @volatile private var running = true
    private val start = LocalDateTime.now()

    require(threadsCount > 0, {
        "Threads count must be greater than 0"
    })
    val runnables = new Array[Thread](threadsCount)

    for (i <- 0 until threadsCount) {
        val thread: Thread = new Thread(new Runnable {
            override def run() {
                while (running) {
                    while (!tasks.isEmpty) {
                        var task: Option[Task[_]] = None
                        try {
                            task = Some(tasks.take())

                            LOGGER.debug("start " + task)
                            val result = task.orNull.getCallable().call()
                            LOGGER.debug("stop " + task + " with " + result)

                            task.orNull.setResult(result)
                            submittedTasks.decrementAndGet()
                        } catch {
                            case e: Exception =>
                                task.foreach(t => {
                                    t.setException(e)
                                    submittedTasks.decrementAndGet()
                                })
                        }
                    }
                }
            }
        }
        )

        thread.start()
        runnables(i) = thread
    }

    /**
      * {@inheritDoc }
      */
    override def submit(task: Task[_]) {
        LOGGER.debug("submit " + task)
        tasks.add(task)
        submittedTasks.incrementAndGet()
    }

    /**
      * {@inheritDoc }
      */
    override def waitUntilFinished() {
        while (submittedTasks.get() != 0) {
        }
    }

    /**
      * {@inheritDoc }
      */
    override def stop(): Duration = {
        running = false
        Duration.between(start, LocalDateTime.now())
    }
}
