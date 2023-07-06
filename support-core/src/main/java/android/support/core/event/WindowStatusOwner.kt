package android.support.core.event

import android.support.core.flow.ErrorFlow
import android.support.core.flow.LoadingFlow
import android.support.core.livedata.ErrorLiveData
import android.support.core.livedata.LoadingLiveData

interface WindowStatusOwner {
    val error: ErrorEvent
    val loading: LoadingEvent
    val refreshLoading: LoadingEvent
}

class LiveDataStatusOwner : WindowStatusOwner {
    override val error: ErrorEvent = ErrorLiveData()
    override val loading: LoadingEvent = LoadingLiveData()
    override val refreshLoading: LoadingEvent = LoadingLiveData()
}

class FlowStatusOwner : WindowStatusOwner {
    override val error: ErrorEvent = ErrorFlow()
    override val loading: LoadingEvent = LoadingFlow()
    override val refreshLoading: LoadingEvent = LoadingLiveData()
}