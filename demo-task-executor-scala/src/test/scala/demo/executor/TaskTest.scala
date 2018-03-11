package demo.executor

import demo.CallableHelper._
import org.testng.Assert.{assertEquals, assertTrue, fail}
import org.testng.annotations.Test

/**
  * Unit tests for [demo.executor.Task].
  */
@Test
class TaskTest extends AbstractConcurrencyTest {

    private val RESULT = "result"
    private val EXCEPTION = new RuntimeException()

    @Test
    def testSetResult() {
        val consumer = new java.util.HashSet[String]()
        val task = getTestTaskWithResult(consumer)
        assertEquals(task.isFinished(), false)

        task.setResult(RESULT)

        assertEquals(task.isFinished(), true)
        assertEquals(consumer.size, 1)
        assertTrue(consumer.contains(RESULT))
    }

    @Test
    def testSetException() {
        val consumer = new java.util.HashSet[Exception]()
        val task = getTaskWithException(consumer)
        assertEquals(task.isFinished(), false)

        task.setException(EXCEPTION)

        assertEquals(task.isFinished(), true)
        assertEquals(consumer.size, 1)
        assertTrue(consumer.contains(EXCEPTION))
    }

    @Test
    def testGetCallableWithResult() {
        val task = getTestTaskWithResult(new java.util.HashSet[String]())
        assertEquals(task.getCallable().call(), RESULT)
    }

    @Test(expectedExceptions = Array(classOf[RuntimeException]))
    def testGetCallableWithException() {
        val task = getTaskWithException(new java.util.HashSet[Exception]())
        task.getCallable().call()
    }

    @Test(expectedExceptions = Array(classOf[IllegalStateException]))
    def testMarkFinishedException() {
        val task = getTestTaskWithResult(new java.util.HashSet[String]())
        task.setResult(RESULT)
        task.markFinished()
    }

    private def getTestTaskWithResult(consumer: java.util.Set[String]): Task[String] = {
        new Task("task with result", classOf[String],
            callable { () => RESULT }
            , { (result: Option[String], exception: Option[Exception]) =>
                if (exception.nonEmpty) {
                    fail()
                } else {
                    consumer.add(result.get)
                }
            })
    }

    private def getTaskWithException(consumer: java.util.Set[Exception]): Task[String] = {
        new Task("task with exception", classOf[String],
            callable { () => throw EXCEPTION }
            , { (result: Option[String], exception: Option[Exception]) =>
                if (exception.nonEmpty) {
                    consumer.add(exception.get)
                } else {
                    fail()
                }
            })
    }
}
