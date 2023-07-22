package com.example.nailexpress.base

import android.app.Application
import android.support.core.livedata.LoadingLiveData

abstract class BaseRefreshViewModel(application: Application) : BaseViewModel(application) {
    val refreshLoading = LoadingLiveData()

    abstract fun onSwipeRefreshData()
}

