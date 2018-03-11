package demo.executor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Abstract unit test for {{@link demo.executor.AsyncExecutor}}.
 */
abstract public class AbstractAsyncExecutorTest extends AbstractConcurrencyTest {

    protected static final int TASK_DURATION_MSEC = 1000;

    protected Task<Integer> getTestTask(int value, Action<Integer> action) {
        return getTestTask(value, Integer.class, action);
    }

    protected Task<Integer> getTestTaskWithResult(int value) {
        return getTestTaskWithResult(value, Integer.class);
    }

    protected Task<Integer> getTestTaskWithException(int value) {
        return getTestTaskWithException(value, Integer.class);
    }

    protected Task getTestTask(List<Integer> consumer, int value) {
        return getTestTask(value,
                (result, exception) -> {
                    if (exception.isPresent()) {
                        fail();
                    } else {
                        consumer.add(value);
                    }
                }
        );
    }

    private <T> Task<T> getTestTask(T value, Class<T> clazz, Action<T> action) {
        return new Task<>("task " + value, clazz,
                () -> {
                    TimeUnit.MILLISECONDS.sleep(TASK_DURATION_MSEC);
                    return value;
                },
                action
        );
    }

    private <T> Task<T> getTestTaskWithResult(T value, Class<T> clazz) {
        return new Task<>("task " + value, clazz,
                () -> {
                    TimeUnit.MILLISECONDS.sleep(TASK_DURATION_MSEC);
                    return value;
                },
                (result, exception) -> {
                    if (exception.isPresent()) {
                        fail();
                    } else {
                        assertEquals(result.get(), value);
                    }
                }
        );
    }

    private <T> Task<T> getTestTaskWithException(T value, Class<T> clazz) {
        return new Task<>("task " + value, clazz,
                () -> {
                    TimeUnit.MILLISECONDS.sleep(TASK_DURATION_MSEC);
                    throw new IllegalArgumentException();
                },
                (result, exception) -> {
                    if (exception.isPresent()) {
                        assertTrue(exception.get() instanceof IllegalArgumentException);
                    } else {
                        fail();
                    }
                }
        );
    }
}
