package android.support.core.event

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

interface Event<T> {
    fun post(value: T)

    fun observe(owner: LifecycleOwner, observer: Observer<in T>)

    fun Flow<T>.observe(owner: LifecycleOwner, doNotify: (T) -> Unit) {
        with(owner) {
            val scope = if (owner is Fragment) owner.viewLifecycleOwner.lifecycleScope
            else lifecycleScope

            scope.launchWhenStarted {
                collect { doNotify(it) }
            }
        }
    }
}

interface ErrorEvent : Event<Throwable>

interface LoadingEvent : Event<Boolean> {
    fun start() {
        post(true)
    }

    fun stop() {
        post(false)
    }
}