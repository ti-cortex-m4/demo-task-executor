package demo.executor;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Unit test for {{@link demo.executor.AsyncExecutor}} to test multiple instances.
 */
@Test
public class AsyncExecutorMultipleInstancesTest extends AbstractAsyncExecutorTest {

    @Test
    public void testMultipleInstances() {
        final int TASKS_COUNT = 4 * 2;

        List<Integer> consumer1 = new ArrayList<>();
        List<Integer> consumer2 = new ArrayList<>();

        Executor executor1 = new AsyncExecutor(1);
        Executor executor2 = new AsyncExecutor(1);

        for (int i = 0; i < TASKS_COUNT; i++) {
            if (i % 2 == 0) {
                Task<?> task1 = getTestTask(consumer1, i);
                executor1.submit(task1);
            } else {
                Task<?> task2 = getTestTask(consumer2, i);
                executor2.submit(task2);
            }
        }

        executor1.waitUntilFinished();
        executor1.stop();

        executor2.waitUntilFinished();
        executor2.stop();

        assertEquals(consumer1.size(), TASKS_COUNT / 2);
        assertEquals(consumer2.size(), TASKS_COUNT / 2);

        for (int i = 0; i < TASKS_COUNT / 2; i++) {
            if (i % 2 == 0) {
                assertEquals((int) consumer1.get(i), i * 2);
            } else {
                assertEquals((int) consumer2.get(i), i * 2 + 1);
            }
        }
    }
}
