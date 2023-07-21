package com.example.nailexpress.datasource

import android.support.core.flow.stateFlowOf
import com.example.nailexpress.models.ui.main.Skill

class AppEvent2 {
    val selectedService = stateFlowOf(Skill())
}