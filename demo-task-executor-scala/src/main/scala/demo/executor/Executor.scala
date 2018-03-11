package demo.executor

import java.time.Duration

/**
 * Executor to run multiple [demo.executor.Task].
 */
trait Executor {

    /**
     * Submits a task to the executor.

     * @param task the task to run.
     */
    def submit(task: Task[_])

    /**
     * Waits until all the submitted  tasks are finished.
     */
    def waitUntilFinished()

    /**
     * Stops the executor after finishing all previously submitted tasks.

     * @return the executor work duration.
     */
    def stop(): Duration
}
