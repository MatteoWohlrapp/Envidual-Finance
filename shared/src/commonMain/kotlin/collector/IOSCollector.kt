package collector

import kotlinx.coroutines.flow.FlowCollector

class IOSCollector<T>(
    private val viewUpdate: (List<T>) -> Unit
): FlowCollector<List<T>> {

    override suspend fun emit(value: List<T>) {
        viewUpdate(value)
    }
}