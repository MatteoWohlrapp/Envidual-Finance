package domain.use_cases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val backgroundDispatcher: CoroutineDispatcher
    get() = Dispatchers.Default
actual val mainDispatcher: CoroutineDispatcher
    get() = Dispatchers.Main