package com.example.nailexpress.views.ui.main.staff.booking_detail.viewmodel

import android.app.Application
import com.example.nailexpress.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingDetailStaffViewModel @Inject constructor(
    application: Application
) : BaseViewModel(application) {
}