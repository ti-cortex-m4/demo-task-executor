package demo

import java.util.concurrent.Callable

object CallableHelper {

    def callable[T](f: () => T): Callable[T] = {
        new Callable[T]() {
            def call() = f()
        }
    }
}
