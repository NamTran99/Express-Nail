package com.example.nailexpress.views.ui.main.staff.booking_detail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.extension.launch
import com.example.nailexpress.models.response.BookingDTO
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.repository.SalonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BookingDetailStaffViewModel @Inject constructor(
    application: Application,
    private val recruitmentBookingStaffRepository: RecruitmentBookingStaffRepository,
    private val salonRepository: SalonRepository
) : BaseViewModel(application) {

    val dataBookingDetail: MutableLiveData<BookingDTO?> = MutableLiveData()
    val salonData: MutableLiveData<Salon?> = MutableLiveData()

    fun getBookingById(id: Int) = launch {
        recruitmentBookingStaffRepository.getDetailBookingById(id).onEach {
            dataBookingDetail.value = it
        }.collect()
    }

    fun getSalonById(id: Int) = launch {
        salonRepository.getSalonByID(id).onEach {
            salonData.value = it
        }.collect()
    }

}