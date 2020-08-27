package domain.use_cases

import platform.Foundation.NSDate
import platform.Foundation.now
import platform.Foundation.timeIntervalSince1970


actual fun getTimestamp(): Long {
    return NSDate.now.timeIntervalSince1970.toLong()
}