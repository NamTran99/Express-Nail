package com.example.nailexpress.views.ui.main.staff.booking_detail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.app.BookingStatusDefine
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.extension.launch
import com.example.nailexpress.helper.DriverUtils
import com.example.nailexpress.models.response.BookingDTO
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.repository.SalonRepository
import com.example.nailexpress.views.widgets.IBookingStatusAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BookingDetailStaffViewModel @Inject constructor(
    application: Application,
    private val recruitmentBookingStaffRepository: RecruitmentBookingStaffRepository,
    private val salonRepository: SalonRepository,
) : BaseViewModel(application), IBookingStatusAction {
    val dataBookingDetail: MutableLiveData<BookingDTO?> = MutableLiveData()
    val salonData: MutableLiveData<Salon?> = MutableLiveData()
    var showDeniedDialog: (()->Unit)? = null

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

    private fun changeStatus(idBooking: Int, status: Int, message: String? = null) {
        launch {
            recruitmentBookingStaffRepository.changeStatusBooking(idBooking, status, message).onEach {
                getBookingById(idBooking)
            }.collect()
        }
    }

    //khônmg remove đi vì đang sử dụng chung bằng cách find method :))
    fun changeStatusBookingDeniedAfterShowDialog(message: String = "") {
        dataBookingDetail.value?.id?.let {
            changeStatus(it, BookingStatusDefine.Deny.bookingStatus, message)
        }
    }

    override fun denied(id: Int) {
        showDeniedDialog?.invoke()
    }

    override fun accept(id: Int) {
        changeStatus(id, BookingStatusDefine.Accept.bookingStatus)
    }

    override fun finish(id: Int) {
        changeStatus(id, BookingStatusDefine.Finish.bookingStatus)
    }

    override fun startMoveToRendezvous(id: Int) {
        changeStatus(id, BookingStatusDefine.StartMoving.bookingStatus)
    }

    override fun iHaveArived(id: Int) {
        changeStatus(id, BookingStatusDefine.Arrived.bookingStatus)
    }

    override fun message() {
        dataBookingDetail.value?.contact_phone?.let {
            DriverUtils.message(getApplication(), it)
        }
    }

    override fun call() {
        dataBookingDetail.value?.contact_phone?.let {
            DriverUtils.call(getApplication(), it)
        }
    }
}