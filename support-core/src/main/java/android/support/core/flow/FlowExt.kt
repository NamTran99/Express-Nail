package android.support.core.flow

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


interface FlowExt {

    private val coScope
        get() = when (this) {
            is ViewModel -> viewModelScope
            is LifecycleOwner -> lifecycleScope
            else -> error("Not support state flow for ${this.javaClass.name}")
        }

    fun <T> Flow<T>.asState(initial: T): SupportStateFlow<T> {
        return asState(coScope, initial)
    }

    fun <T> Flow<List<T>>.asState(): SupportStateFlow<List<T>> {
        return asState(emptyList())
    }

    fun <T> Flow<T>.launch(): Job = coScope.launch {
        collect()
    }
}

fun <T> mutableSharedFlow() = MutableSharedFlow<T>()

fun <T> Flow<T>.asState(scope: CoroutineScope, initial: T): SupportStateFlow<T> {
    val state = SupportStateFlowImpl(initial)
    val self = this
    scope.launch {
        state.emitAll(self)
    }
    return state
}

fun <T> Flow<List<T>>.asState(scope: CoroutineScope): SupportStateFlow<List<T>> {
    return asState(scope, emptyList())
}

interface SupportStateFlow<T> : MutableStateFlow<T> {
    fun post(value: T)
}

class SupportStateFlowImpl<T>(initial: T) : SupportStateFlow<T>,
    MutableStateFlow<T> by MutableStateFlow(initial) {
    override fun post(value: T) {
        if (this.value == value) return
        this.value = value
    }
}