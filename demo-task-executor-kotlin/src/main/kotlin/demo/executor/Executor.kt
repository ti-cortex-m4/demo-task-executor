package demo.executor

import java.time.Duration

/**
 * Executor to run multiple [demo.executor.Task].
 */
interface Executor {

    /**
     * Submits a task to the executor.

     * @param task the task to run.
     */
    fun submit(task: Task<*>)

    /**
     * Waits until all the submitted  tasks are finished.
     */
    fun waitUntilFinished()

    /**
     * Stops the executor after finishing all previously submitted tasks.

     * @return the executor work duration.
     */
    fun stop(): Duration
}
