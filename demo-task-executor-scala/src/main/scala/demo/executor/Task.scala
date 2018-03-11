package demo.executor

import java.time.{Duration, LocalDateTime}
import java.util.Objects
import java.util.concurrent.Callable

/**
  * Task that combines [java.util.concurrent.Callable] as producer and [demo.executor.Action] as consumer.
  * This class IS thread-safe.
  */
final class Task[T](name: String, clazz: Class[T], callable: Callable[T], action: (Option[T], Option[Exception]) => Unit) {

    private val this.name = Objects.requireNonNull(name)
    private val this.clazz = Objects.requireNonNull(clazz)
    private val this.callable = Objects.requireNonNull(callable)
    private val this.action = Objects.requireNonNull(action)

    @volatile var finished = false
    private val start = LocalDateTime.now()
    private var duration: Option[Duration] = None

    def getCallable(): Callable[T] = {
        callable
    }

    def isFinished(): Boolean = {
        finished
    }

    def markFinished() {
        if (finished) {
            throw new IllegalStateException("Task must be non-finished")
        }
        duration = Some[Duration](Duration.between(start, LocalDateTime.now()))
        finished = true
    }

    def setResult(result: Any) {
        this.synchronized {
            action(Some(clazz.cast(result)), None)
            markFinished()
        }
    }

    def setException(exception: Exception) {
        this.synchronized {
            action(None, Some(Objects.requireNonNull(exception)))
            markFinished()
        }
    }

    /**
      * Waits until the task is finished.
      *
      * @return the task duration.
      */
    def waitUntilFinished(): Duration = {
        while (!finished) {
        }
        require(duration.nonEmpty, {
            "Duration must be set"
        })
        duration.get
    }

    override def toString: String = {
        "Task{'" + name + "'}"
    }
}
