package cache

import platform.Foundation.NSThread

actual fun getThread(): String {
    return NSThread.isMainThread.toString()
}