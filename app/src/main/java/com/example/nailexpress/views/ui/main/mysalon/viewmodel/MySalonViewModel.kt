package com.example.nailexpress.views.ui.main.mysalon.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.extension.launch
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.repository.SalonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MySalonViewModel @Inject constructor(
    application: Application,
    private val salonRepository: SalonRepository
) : BaseViewModel(application) {

    init {
        getMySalon()
    }

    val salonData: MutableLiveData<Salon> = MutableLiveData()

    private fun getMySalon() = launch {
        salonRepository.getSalonDetail().onEach { salons ->
            salonData.value = salons.getOrNull(0)
        }.collect()
    }
}