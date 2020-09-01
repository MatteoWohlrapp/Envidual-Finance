package domain.use_cases

import platform.Foundation.NSDate
import platform.Foundation.addTimeInterval
import platform.Foundation.now

actual fun getTodaysDate(): String {
    val date = NSDate.now
    val string = date.toString()
    val splitted = string.split(" ")
    return splitted[0]
}

actual fun getYesterdaysDate(): String {
    val today = NSDate.now
    val yesterday = today.addTimeInterval(-86400.0)
    val string = yesterday.toString()
    val splitted = string.split(" ")
    return splitted[0]
}