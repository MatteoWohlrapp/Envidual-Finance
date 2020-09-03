package collector

import kotlinx.coroutines.flow.FlowCollector

class IOSCollector<T>(
    private val viewUpdate: (T) -> Unit
): FlowCollector<T> {

    override suspend fun emit(value: T) {
        viewUpdate(value)
    }
}