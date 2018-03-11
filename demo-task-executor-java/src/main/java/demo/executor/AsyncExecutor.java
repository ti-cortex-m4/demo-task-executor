package demo.executor;

import demo.logger.ConsoleLogger;
import demo.logger.FileLogger;
import demo.logger.Logger;
import demo.logger.LoggerFactory;
import demo.logger.Severity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Executor to run multiple {@link demo.executor.Task} asynchronously.
 */
final public class AsyncExecutor implements Executor {

    private static final Logger LOGGER = new LoggerFactory(AsyncExecutor.class)
            .add(new FileLogger("demo-task-executor-java.log"), Severity.DEBUG)
            .add(new ConsoleLogger(), Severity.TRACE)
            .build();

    private final BlockingQueue<Task<?>> tasks = new LinkedBlockingQueue<>();
    private final AtomicInteger submittedTasks = new AtomicInteger(0);

    private volatile boolean running = true;
    private LocalDateTime start = LocalDateTime.now();

    /**
     * Creates an executor with given threads count.
     *
     * @param threadsCount threads count for the executor.
     */
    public AsyncExecutor(int threadsCount) {
        if (threadsCount == 0) {
            throw new IllegalArgumentException("Threads count must be greater than 0");
        }
        final Runnable[] runnables = new Thread[threadsCount];

        for (int i = 0; i < threadsCount; i++) {
            Thread thread = new Thread(() -> {
                while (running) {
                    while (!tasks.isEmpty()) {
                        Optional<Task<?>> task = Optional.empty();
                        try {
                            task = Optional.of(tasks.take());

                            LOGGER.debug("start " + task);
                            Object result = task.get().getCallable().call();
                            LOGGER.debug("stop " + task + " with " + result);

                            task.get().setResult(result);
                            submittedTasks.decrementAndGet();
                        } catch (Exception e) {
                            task.ifPresent(t -> {
                                t.setException(e);
                                submittedTasks.decrementAndGet();
                            });
                        }
                    }
                }
            });

            thread.start();
            runnables[i] = thread;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void submit(Task<?> task) {
        LOGGER.debug("submit " + task);
        tasks.add(task);
        submittedTasks.incrementAndGet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void waitUntilFinished() {
        while (submittedTasks.get() != 0) ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration stop() {
        running = false;
        return Duration.between(start, LocalDateTime.now());
    }
}
