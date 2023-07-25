package com.example.nailexpress.views.ui.main.customer.detailpost.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.extension.launch
import com.example.nailexpress.models.response.RecruitmentDataDTO
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.repository.SalonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailPostCustomerVM @Inject constructor(
    application: Application,
    private val recruitmentBookingStaffRepository: RecruitmentBookingStaffRepository,
    private val salonRepository: SalonRepository
) : BaseViewModel(application) {

    val dataRecruitment = MutableLiveData<RecruitmentDataDTO>()
    val dataSalon = MutableLiveData<Salon>()

    fun getRecruitmentById(id: Int) = launch {
        recruitmentBookingStaffRepository.getRecruitmentById(id).onEach {
            dataRecruitment.value = it
        }.collect()
    }

    fun getSalonById(id: Int) = launch {
        salonRepository.getSalonByID(id).onEach {
            dataSalon.value = it
        }.collect()
    }
}