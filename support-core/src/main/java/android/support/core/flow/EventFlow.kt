package android.support.core.flow

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import java.util.concurrent.atomic.AtomicBoolean

fun <T> singleEventFlowOf(): SingleEventFlow<T> = SingleEventFlowImpl<T>()

fun singleEventFlow(): SingleEventFlow<Unit> = SingleEventFlowImpl<Unit>()

interface SingleEventFlow<T> : Flow<T>, FlowCollector<T> {
    fun post(value: T)
}

private open class SingleEventFlowImpl<T>(
    private val flow: MutableSharedFlow<T> = MutableSharedFlow<T>(1, 1, BufferOverflow.DROP_OLDEST)
) : SingleEventFlow<T> {
    private val mPending = AtomicBoolean(false)

    override suspend fun collect(collector: FlowCollector<T>) {
        flow.collect {
            if (mPending.compareAndSet(true, false)) {
                collector.emit(it)
            }
        }
    }

    override fun post(value: T) {
        mPending.set(true)
        flow.tryEmit(value)
    }

    override suspend fun emit(value: T) {
        mPending.set(true)
        flow.emit(value)
    }
}

fun SingleEventFlow<Unit>.call() {
    post(Unit)
}