package domain.use_cases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

expect val backgroundDispatcher : CoroutineDispatcher
expect val mainDispatcher: CoroutineDispatcher