package com.example.nailexpress.repository

import android.content.Context
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.datasource.remote.RecruitmentBookingStaffApi
import com.example.nailexpress.factory.BookingCvFactory
import com.example.nailexpress.models.ui.main.BookingStaffForm
import kotlinx.coroutines.flow.flow

class BookingStaffRepository(
    val userDataSource: SharePrefs,
    context: Context,
    val api: RecruitmentBookingStaffApi,
    val cvFactory: BookingCvFactory
) {
    suspend fun getListBookingStaff(search: String = "", page: Int = 1) = flow {
        emit(
            cvFactory.createListBooking(
                api.getListMyBooking(search, page = page).await()
            )
        )
    }

    suspend fun getBookingById(id: Int) = flow {
        emit(
            cvFactory.createBooking(
                api.getBookingById(id).await()
            )
        )
    }

    suspend fun bookingStaff(form: BookingStaffForm) {
        api.bookStaff(form).await()
    }
}