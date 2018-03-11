package demo.executor;

import java.time.Duration;

/**
 * Executor to run multiple {@link demo.executor.Task}.
 */
public interface Executor {

    /**
     * Submits a task to the executor.
     *
     * @param task the task to run.
     */
    void submit(Task<?> task);

    /**
     * Waits until all the submitted  tasks are finished.
     */
    void waitUntilFinished();

    /**
     * Stops the executor after finishing all previously submitted tasks.
     *
     * @return the executor work duration.
     */
    Duration stop();
}
