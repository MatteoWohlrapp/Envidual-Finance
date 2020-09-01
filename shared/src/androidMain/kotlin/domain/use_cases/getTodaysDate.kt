package domain.use_cases

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
actual fun getTodaysDate(): String {
    val date = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return date.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
actual fun getYesterdaysDate(): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return yesterday.format(formatter)
}