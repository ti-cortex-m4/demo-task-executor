package demo.logger

/**
 * Logging severity.
 */
enum class Severity private constructor(val value: Int) {

    TRACE(1),
    DEBUG(2),
    INFO(3),
    WARN(4),
    ERROR(5)
}
