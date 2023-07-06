package android.support.core.livedata

import android.annotation.SuppressLint
import android.support.core.event.ErrorEvent
import android.util.Log
import androidx.arch.core.executor.ArchTaskExecutor

class ErrorLiveData : SingleLiveEvent<Throwable>(), ErrorEvent {
    @SuppressLint("RestrictedApi")
    override fun post(value: Throwable) {
            if (ArchTaskExecutor.getInstance().isMainThread) this.value = value
            else postValue(value)
    }
}