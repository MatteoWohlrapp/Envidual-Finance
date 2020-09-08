package co.touchlab.kampkit.flowcollector

import kotlinx.coroutines.flow.FlowCollector

class CustomFlowCollector<T>(
    private val viewUpdate: (List<T>) -> Unit
): FlowCollector<List<T>> {

    override suspend fun emit(value: List<T>) {
        viewUpdate(value)
    }
}