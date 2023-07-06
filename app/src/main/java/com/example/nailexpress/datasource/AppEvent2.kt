package com.example.nailexpress.datasource

import android.support.core.flow.stateFlowOf
import android.support.core.livedata.SingleLiveEvent
import com.example.nailexpress.models.ui.main.Service
import kotlinx.coroutines.flow.StateFlow

class AppEvent2 {
    val selectedService = stateFlowOf(Service())
}