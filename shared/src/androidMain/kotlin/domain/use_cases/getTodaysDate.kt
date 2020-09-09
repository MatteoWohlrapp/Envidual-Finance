package domain.use_cases

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
actual fun getDayNumberOfDaysBefore(numberOfDays: Int): String {
    val today = LocalDate.now()
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayBefore = today.minusDays(numberOfDays.toLong())
    return dayBefore.format(format)
}