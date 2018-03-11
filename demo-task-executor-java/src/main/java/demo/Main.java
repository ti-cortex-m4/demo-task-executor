package demo;

import demo.executor.AsyncExecutor;
import demo.executor.Executor;
import demo.executor.Task;
import demo.logger.ConsoleLogger;
import demo.logger.FileLogger;
import demo.logger.Logger;
import demo.logger.LoggerFactory;
import demo.logger.Severity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger LOGGER = new LoggerFactory(Main.class)
            .add(new FileLogger("demo-task-executor-java.log"), Severity.DEBUG)
            .add(new ConsoleLogger(), Severity.TRACE)
            .build();

    private static final int THREADS_COUNT = 2;

    public static void main(String[] args) throws InterruptedException {
        LOGGER.trace("Start application");
        LocalDateTime start = LocalDateTime.now();

        Executor executor = new AsyncExecutor(THREADS_COUNT);

        Task<String> task1 = new Task<>("task1", String.class,
                () -> {
                    TimeUnit.SECONDS.sleep(3);
                    return "OK";
                },
                Main::accept
        );

        Task<Integer> task2 = new Task<>("task2", Integer.class,
                () -> {
                    TimeUnit.SECONDS.sleep(1);
                    return 0;
                },
                Main::accept
        );

        Task<Float> task3 = new Task<>("task3", Float.class,
                () -> {
                    TimeUnit.SECONDS.sleep(1);
                    throw new RuntimeException();
                },
                Main::accept
        );

        executor.submit(task1);
        executor.submit(task2);
        executor.submit(task3);

        executor.waitUntilFinished();
        executor.stop();

        LOGGER.trace("Stop application in " + Duration.between(start, LocalDateTime.now()).getSeconds() + " seconds");
    }

    private static <T> void accept(Optional<T> result, Optional<Exception> exception) {
        if (exception.isPresent()) {
            LOGGER.debug("exception: " + exception);
        } else {
            LOGGER.debug("result: " + result);
        }
    }
}
