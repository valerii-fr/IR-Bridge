package dev.nordix.irbridge.core.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transformLatest
import kotlin.time.Duration

fun tickerFlow(ms: Long = 1000L) = flow {
    while (true) {
        emit(Unit)
        delay(ms)
    }
}

fun tickerFlow(duration: Duration) = flow {
    while (true) {
        emit(Unit)
        delay(duration)
    }
}

fun <T> Flow<T>.debounceIf(
    timeoutMillis: Long,
    predicate: (T) -> Boolean
): Flow<T> = transformLatest { value ->
    if (predicate(value)) {
        delay(timeoutMillis)
    }
    emit(value)
}
fun <T> Flow<T>.debounceIfWithPrevious(
    timeoutMillis: Long,
    predicate: (prev: T?, cur: T) -> Boolean
): Flow<T> = flow {
    var prev: T? = null

    this@debounceIfWithPrevious.collect { value ->
        val shouldDebounce = predicate(prev, value)
        prev = value

        if (shouldDebounce) {
            delay(timeoutMillis)
        }

        emit(value)
    }
}
