package com.example.nailexpress.datasource

import android.support.core.flow.stateFlowOf
import android.support.core.livedata.SingleLiveEvent
import com.example.nailexpress.models.ui.main.SearchCityStateForm
import com.example.nailexpress.models.ui.main.Skill

class AppEvent2 {
    val selectedService = stateFlowOf(Skill())
    val findingWorkingArea = SingleLiveEvent<SearchCityStateForm>()
}