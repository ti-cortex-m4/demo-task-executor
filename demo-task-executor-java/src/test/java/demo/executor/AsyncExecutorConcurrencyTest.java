package demo.executor;

import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Unit test for {{@link demo.executor.AsyncExecutor}} to test concurrency.
 */
@Test
public class AsyncExecutorConcurrencyTest extends AbstractAsyncExecutorTest {

    @Test
    public void testConcurrencyDuration() {
        assertAlmostEquals(testConcurrencyDuration(1), TASK_DURATION_MSEC * 4);
        assertAlmostEquals(testConcurrencyDuration(2), TASK_DURATION_MSEC * 2);
        assertAlmostEquals(testConcurrencyDuration(4), TASK_DURATION_MSEC);
    }

    private long testConcurrencyDuration(int threadsCount) {
        final int TASKS_COUNT = 4;

        Executor executor = new AsyncExecutor(threadsCount);
        LocalDateTime start = LocalDateTime.now();

        for (int i = 0; i < TASKS_COUNT; i++) {
            Task<?> task = getTestTaskWithResult(i);
            executor.submit(task);
        }

        executor.waitUntilFinished();
        executor.stop();

        Duration duration = Duration.between(start, LocalDateTime.now());
        return duration.getSeconds() * 1000;
    }
}
