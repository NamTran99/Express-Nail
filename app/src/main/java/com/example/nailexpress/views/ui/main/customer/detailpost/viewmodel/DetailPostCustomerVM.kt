package com.example.nailexpress.views.ui.main.customer.detailpost.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nailexpress.base.BaseRefreshViewModel
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.extension.launch
import com.example.nailexpress.models.response.RecruitmentDataDTO
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPostCustomerVM @Inject constructor(
    application: Application,
    private val recruitmentBookingStaffRepository: RecruitmentBookingStaffRepository
) : BaseRefreshViewModel(application) {

    val data = MutableLiveData<RecruitmentDataDTO>()

    fun getRecruitmentById(id: Int) = launch {
        recruitmentBookingStaffRepository.getRecruitmentById(id).onEach {
            data.value = it
        }.collect()
    }

    override fun onSwipeRefreshData() {
    }
}