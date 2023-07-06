package android.support.core.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class CoroutineMediatorLiveData<T>(timeout: Long = 5000) : MediatorLiveData<T>() {
    private val mScope = LiveDataScope(timeout)
    protected val scope get() = mScope.scope ?: error("My Scope not initialized yet!")

    override fun onInactive() {
        super.onInactive()
        mScope.tryToClose(shouldCancelClose = { hasActiveObservers() })
    }

    fun <R> addSourceSuspendable(
        source: LiveData<R>,
        function: suspend CoroutineScope. (R) -> Unit,
    ) {
        super.addSource(source) {
            mScope.getOrCreate().launch { function(it) }
        }
    }
}