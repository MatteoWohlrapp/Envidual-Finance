package domain.use_cases

import platform.Foundation.NSDate
import platform.Foundation.addTimeInterval
import platform.Foundation.now

actual fun getDayNumberOfDaysBefore(numberOfDays: Int): String {
    val today = NSDate.now
    val dayBefore = today.addTimeInterval(numberOfDays*(-86400.00))
    val dayString = dayBefore.toString()
    val splittedDates = dayString.split(" ")
    return splittedDates[0]
}