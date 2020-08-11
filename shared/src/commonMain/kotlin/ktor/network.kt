package co.touchlab.kampkit.ktor

internal expect suspend fun <R> network(block: suspend () -> R): R