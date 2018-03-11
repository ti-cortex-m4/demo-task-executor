package demo.executor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Task that combines {@link java.util.concurrent.Callable} as producer and {@link demo.executor.Action} as consumer.
 * This class IS thread-safe.
 */
final public class Task<T> {

    final private String name;
    final private Class<T> clazz;
    final private Callable<T> callable;
    final private Action<T> action;

    private volatile boolean finished = false;
    private LocalDateTime start = LocalDateTime.now();
    private Optional<Duration> duration = Optional.empty();

    /**
     * Creates an task.
     *
     * @param name     the task name.
     * @param clazz    the result class type token to cast generic type at runtime.
     * @param callable the {@link java.util.concurrent.Callable} to run.
     * @param action   the {@link demo.executor.Action} to process result or exception.
     */
    public Task(String name, Class<T> clazz, Callable<T> callable, Action<T> action) {
        this.name = Objects.requireNonNull(name);
        this.clazz = Objects.requireNonNull(clazz);
        this.callable = Objects.requireNonNull(callable);
        this.action = Objects.requireNonNull(action);
    }

    Callable<T> getCallable() {
        return this.callable;
    }

    boolean isFinished() {
        return finished;
    }

    void markFinished() {
        if (finished) {
            throw new IllegalStateException("Task must be non-finished");
        }
        duration = Optional.of(Duration.between(start, LocalDateTime.now()));
        finished = true;
    }

    synchronized void setResult(Object result) {
        this.action.accept(Optional.of(clazz.cast(result)), Optional.empty());
        markFinished();
    }

    synchronized void setException(Exception exception) {
        this.action.accept(Optional.empty(), Optional.of(Objects.requireNonNull(exception)));
        markFinished();
    }

    /**
     * Waits until the task is finished.
     *
     * @return the task duration.
     */
    public Duration waitUntilFinished() {
        while (!finished) ;
        if (!duration.isPresent()) {
            throw new IllegalStateException("Duration must be set");
        }
        return duration.get();
    }

    @Override
    public String toString() {
        return "Task{" + "'" + name + '\'' + '}';
    }
}
