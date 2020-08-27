package domain.use_cases

import java.time.LocalDate

actual fun getTimestamp(): Long {
    return System.currentTimeMillis()/1000
}