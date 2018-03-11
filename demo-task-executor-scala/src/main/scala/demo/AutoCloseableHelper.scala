package demo

object AutoCloseableHelper {

    def use[A <: AutoCloseable, B](resource: A)(block: A => B) = {
        var t: Throwable = null
        try {
            block(resource)
        } catch {
            case x => t = x; throw x
        } finally {
            if (resource != null) {
                if (t != null) {
                    try {
                        resource.close()
                    } catch {
                        case y => t.addSuppressed(y)
                    }
                } else {
                    resource.close()
                }
            }
        }
    }
}
