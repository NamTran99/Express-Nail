package android.support.core.livedata

import android.annotation.SuppressLint
import android.support.core.event.LoadingEvent
import android.util.Log
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.MutableLiveData

class LoadingLiveData : MutableLiveData<Boolean>(), LoadingEvent {
    private var mNumOfLoading = 0

    override fun postValue(value: Boolean?) {
        synchronized(this) {
            if (value!!) {
                mNumOfLoading++
                if (shouldPost(true)) super.postValue(true)
            } else {
                mNumOfLoading--
                if (mNumOfLoading < 0) mNumOfLoading = 0
                if (mNumOfLoading == 0) super.postValue(false)
            }
        }
    }

    private fun shouldPost(b: Boolean) = this.value != b

    @SuppressLint("RestrictedApi")
    override fun post(value: Boolean) {
        if (ArchTaskExecutor.getInstance().isMainThread)
        {
            this.value = value
        }
        else{
            postValue(value)
        }
    }
}