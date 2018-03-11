package demo.executor;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Unit test for {{@link demo.executor.AsyncExecutor}} to test run order of {@link demo.executor.Task}.
 */
@Test
public class AsyncExecutorTaskOrderTest extends AbstractAsyncExecutorTest {

    @Test
    public void testTaskOrder() throws InterruptedException {
        final int TASKS_COUNT = 8;

        List<Integer> consumer = new ArrayList<>();
        Executor executor = new AsyncExecutor(1);

        for (int i = 0; i < TASKS_COUNT; i++) {
            Task<?> task = getTestTask(consumer, i);
            executor.submit(task);
        }

        executor.waitUntilFinished();
        executor.stop();

        assertEquals(consumer.size(), TASKS_COUNT);
        for (int i = 0; i < TASKS_COUNT; i++) {
            assertEquals((int) consumer.get(i), i);
        }
    }
}
