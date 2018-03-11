package demo.logger

/**
 * Logging severity.
 */
sealed trait Severity

case object TRACE extends Severity
case object DEBUG extends Severity
case object INFO extends Severity
case object WARN extends Severity
case object ERROR extends Severity

object SeverityConverter {

    val values = List(
        (1, TRACE),
        (2, DEBUG),
        (3, INFO),
        (4, WARN),
        (5, ERROR)
    )

    def toSeverity(code: Short): Severity = {
        for (value <- values) {
            if (value._1 == code) {
                return value._2
            }
        }
        throw new IllegalArgumentException("Severity not found for the code: " + code)
    }

    def toCode(recordType: Severity): Short = {
        for (value <- values) {
            if (value._2 == recordType) {
                return value._1.toShort
            }
        }
        throw new IllegalArgumentException("Code not found for the Severity: " + recordType)
    }
}