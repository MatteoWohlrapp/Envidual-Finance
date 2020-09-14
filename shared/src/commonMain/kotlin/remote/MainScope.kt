package co.touchlab.kampkit

import co.touchlab.kermit.Kermit
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class MainScope(private val mainContext: CoroutineContext) :
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = mainContext + job + exceptionHandler

    internal val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Exception occured: " + throwable.toString())
    }


    fun onDestroy() {
        job.cancel()
    }
}
