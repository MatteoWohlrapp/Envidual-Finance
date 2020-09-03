package co.touchlab.kampkit.ktor

import kotlin.coroutines.coroutineContext

internal actual suspend fun <R> network(block: suspend () -> R): R {
    println("I am doing a network call on ${coroutineContext.toString()}")
    return block()
}
