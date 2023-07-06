package android.support.core.extensions

import android.support.core.event.Event
import android.support.core.flow.DistributionFlow
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn

interface LifecycleSubscriberExt :  LifecycleOwner {
    private val bindOwner: LifecycleOwner
        get() = if (this is Fragment) viewLifecycleOwner
        else this

    fun <T> Flow<T>.bind(observer: Observer<in T>) {
        val self = this
        bindOwner.lifecycleScope.launchWhenStarted {
            val flow = if (self is StateFlow
                || self is DistributionFlow
            ) self else self
                .stateIn(this)
            flow.collect {
                observer.onChanged(it)
            }
        }
    }

    fun <T> LiveData<T>.bind(observer: Observer<in T>) {
        observe(bindOwner, observer)
    }

    fun <T> Event<T>.bind(observer: Observer<T>) {
        observe(bindOwner, observer)
    }
}