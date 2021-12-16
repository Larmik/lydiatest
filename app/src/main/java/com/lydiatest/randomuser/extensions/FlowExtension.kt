package com.lydiatest.randomuser.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive

fun <T> Flow<T>.bind(target: MutableSharedFlow<T>, scope: CoroutineScope) = onEach { target.emit(it) }.launchIn(scope)

@ExperimentalCoroutinesApi
fun <T> ProducerScope<T>.safeOffer(element: T) {
    if (isActive) trySend(element).isSuccess
}