package demo.executor;

import org.testng.annotations.Test;

/**
 * Unit test for {{@link demo.executor.AsyncExecutor}} to test results and exceptions of {@link demo.executor.Task}.
 */
@Test
public class AsyncExecutorResultExceptionTest extends AbstractAsyncExecutorTest {

    @Test
    public void testResult() {
        Executor executor = new AsyncExecutor(1);

        Task<?> task1 = getTestTaskWithResult(1);
        executor.submit(task1);

        executor.waitUntilFinished();
        executor.stop();
    }

    @Test
    public void testResultResult() {
        Executor executor = new AsyncExecutor(1);

        Task<?> task1 = getTestTaskWithResult(11);
        executor.submit(task1);
        Task<?> task2 = getTestTaskWithResult(12);
        executor.submit(task2);

        executor.waitUntilFinished();
        executor.stop();
    }

    @Test
    public void testException() {
        Executor executor = new AsyncExecutor(1);

        Task<?> task1 = getTestTaskWithException(2);
        executor.submit(task1);

        task1.waitUntilFinished();
        executor.stop();
    }

    @Test
    public void testExceptionException() {
        Executor executor = new AsyncExecutor(1);

        Task<?> task1 = getTestTaskWithException(21);
        executor.submit(task1);
        Task<?> task2 = getTestTaskWithException(22);
        executor.submit(task2);

        executor.waitUntilFinished();
        executor.stop();
    }

    @Test
    public void testResultException() {
        Executor executor = new AsyncExecutor(1);

        Task<?> task1 = getTestTaskWithResult(31);
        executor.submit(task1);
        Task<?> task2 = getTestTaskWithException(32);
        executor.submit(task2);

        executor.waitUntilFinished();
        executor.stop();
    }

    @Test
    public void testExceptionResult() {
        Executor executor = new AsyncExecutor(1);

        Task<?> task1 = getTestTaskWithException(41);
        executor.submit(task1);
        Task<?> task2 = getTestTaskWithResult(42);
        executor.submit(task2);

        executor.waitUntilFinished();
        executor.stop();
    }
}
