package demo.executor;

import java.util.Optional;

/**
 * Action that is performed when {@link demo.executor.Task} is finished.
 */
@FunctionalInterface
public interface Action<T> {

    /**
     * Accepts result or exception when {@link demo.executor.Task} is finished.
     *
     * @param result    optional result.
     * @param exception optional exception.
     */
    void accept(Optional<T> result, Optional<Exception> exception);
}
