package demo.executor

import java.util.concurrent.TimeUnit

import demo.CallableHelper._
import org.testng.Assert._

/**
  * Abstract unit test for {[demo.executor.AsyncExecutor]}.
  */
abstract class AbstractAsyncExecutorTest extends AbstractConcurrencyTest {

    protected val TASK_DURATION_MSEC: Long = 1000

    protected def getTestTask(value: Integer, action: (Option[Integer], Option[Exception]) => Unit): Task[Integer] = {
        getTestTask(value, classOf[Integer], action)
    }

    protected def getTestTaskWithResult(value: Integer): Task[Integer] = {
        getTestTaskWithResult(value, classOf[Integer])
    }

    protected def getTestTaskWithException(value: Integer): Task[Integer] = {
        getTestTaskWithException(value, classOf[Integer])
    }

    protected def getTestTask(consumer: java.util.List[Integer], value: Integer): Task[Integer] = {
        getTestTask(value, { (result: Option[Integer], exception: Option[Exception]) =>
            if (exception.nonEmpty) {
                fail()
            } else {
                consumer.add(value)
            }
        })
    }

    private def getTestTask[T](value: T, clazz: Class[T], action: (Option[T], Option[Exception]) => Unit): Task[T] = {
        new Task("task " + value, clazz,
            callable {
                () => {
                    TimeUnit.MILLISECONDS.sleep(TASK_DURATION_MSEC)
                    value
                }
            },
            action)
    }

    private def getTestTaskWithResult[T](value: T, clazz: Class[T]): Task[T] = {
        new Task("task " + value, clazz,
            callable {
                () => {
                    TimeUnit.MILLISECONDS.sleep(TASK_DURATION_MSEC)
                    value
                }
            }, { (result: Option[T], exception: Option[Exception]) =>
                if (exception.nonEmpty) {
                    fail()
                } else {
                    assertEquals(result.get, value)
                }
            })
    }

    private def getTestTaskWithException[T](value: T, clazz: Class[T]): Task[T] = {
        new Task[T]("task " + value, clazz,
            callable {
                () => {
                    TimeUnit.MILLISECONDS.sleep(TASK_DURATION_MSEC)
                    throw new IllegalArgumentException()
                }
            }, { (result: Option[T], exception: Option[Exception]) =>
                if (exception.nonEmpty) {
                    assertTrue(exception.get.isInstanceOf[IllegalArgumentException])
                } else {
                    fail()
                }
            })
    }
}
