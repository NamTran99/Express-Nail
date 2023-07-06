package android.support.core.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DistributionLiveData<T>(def: T? = null) : MediatorLiveData<T>() {
    private var mSource: LiveData<out T>? = null
    private var mFlowSource: Flow<T>? = null
    private var mFlowConnectedJob: Job? = null
    private val mLiveDataScope = LiveDataScope()

    init {
        if (def != null) post(def)
    }

    fun connect(source: LiveData<out T>) {
        if (mSource == source) return
        if (mSource != null) removeSource(mSource!!)
        mSource = source
        super.addSource(source) { value = it }
    }

    override fun onInactive() {
        super.onInactive()
        mLiveDataScope.tryToClose(shouldCancelClose = { hasActiveObservers() })
    }

    override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        error("Not support")
    }

    fun connect(source: Flow<T>) {
        if (mFlowSource == source) return
        mFlowConnectedJob?.cancel()

        mFlowSource = source
        mFlowConnectedJob = source
            .onEach { post(it) }
            .launchIn(mLiveDataScope.getOrCreate())
    }
}