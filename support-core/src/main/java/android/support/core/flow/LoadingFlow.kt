package android.support.core.flow

import android.support.core.event.LoadingEvent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class LoadingFlow : LoadingEvent {
    private val loading = stateFlowOf(false)

    override fun post(value: Boolean) {
        loading.post(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in Boolean>) {
        loading.observe(owner) {
            observer.onChanged(it)
        }
    }
}