package demo.executor;

import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Unit tests for {@link demo.executor.Task}.
 */
@Test
public class TaskTest extends AbstractConcurrencyTest {

    private static final String RESULT = "result";
    private static final RuntimeException EXCEPTION = new RuntimeException();

    @Test
    public void testSetResult() {
        Set<String> consumer = new HashSet<>();
        Task<String> task = getTestTaskWithResult(consumer);
        assertEquals(task.isFinished(), false);

        task.setResult(RESULT);

        assertEquals(task.isFinished(), true);
        assertEquals(consumer.size(), 1);
        assertTrue(consumer.contains(RESULT));
    }

    @Test
    public void testSetException() {
        Set<Exception> consumer = new HashSet<>();
        Task<String> task = getTaskWithException(consumer);
        assertEquals(task.isFinished(), false);

        task.setException(EXCEPTION);

        assertEquals(task.isFinished(), true);
        assertEquals(consumer.size(), 1);
        assertTrue(consumer.contains(EXCEPTION));
    }

    @Test
    public void testGetCallableWithResult() throws Exception {
        Task<String> task = getTestTaskWithResult(new HashSet<>());
        assertEquals(task.getCallable().call(), RESULT);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testGetCallableWithException() throws Exception {
        Task<String> task = getTaskWithException(new HashSet<>());
        task.getCallable().call();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testMarkFinishedException() {
        Task<String> task = getTestTaskWithResult(new HashSet<>());
        task.setResult(RESULT);
        task.markFinished();
    }

    private Task<String> getTestTaskWithResult(Set<String> consumer) {
        return new Task<>("task with result", String.class,
                () -> {
                    return RESULT;
                },
                (result, exception) -> {
                    if (exception.isPresent()) {
                        fail();
                    } else {
                        consumer.add(result.get());
                    }
                }
        );
    }

    private Task<String> getTaskWithException(Set<Exception> consumer) {
        return new Task<>("task with exception", String.class,
                () -> {
                    throw EXCEPTION;
                },
                (result, exception) -> {
                    if (exception.isPresent()) {
                        consumer.add(exception.get());
                    } else {
                        fail();
                    }
                }
        );
    }
}
