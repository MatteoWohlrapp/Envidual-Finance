package cache

actual fun getThread(): String {
    return Thread.currentThread().name
}