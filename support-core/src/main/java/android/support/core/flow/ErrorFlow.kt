package android.support.core.flow

import android.support.core.event.ErrorEvent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class ErrorFlow : ErrorEvent {
    private val event = singleEventFlowOf<Throwable>()
    override fun post(value: Throwable) {
        event.post(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in Throwable>) {
        event.observe(owner) { observer.onChanged(it) }
    }
}

fun <T> Flow<T>.catchBy(error: ErrorEvent): Flow<T> {
    return this.catch {
        error.post(it)
    }
}