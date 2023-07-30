package com.example.nailexpress.repository

import android.content.Context
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.app.safe
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.datasource.remote.RecruitmentBookingStaffApi
import com.example.nailexpress.extension.buildMultipart
import com.example.nailexpress.extension.toScaleImagePart
import com.example.nailexpress.factory.BookingCvFactory
import com.example.nailexpress.helper.RequestBodyBuilder
import com.example.nailexpress.models.ui.main.BookingStaffForm
import com.example.nailexpress.models.ui.main.RecruitmentForm
import com.example.nailexpress.models.ui.main.Salon
import kotlinx.coroutines.flow.flow

class RecruitmentBookingStaffRepository(
    val userDataSource: SharePrefs,
    context: Context,
    val api: RecruitmentBookingStaffApi,
    val factory: BookingCvFactory,
) {
    suspend fun getListBookingStaff(search: String = "", page: Int = 1) = flow {
        emit(
            factory.createListBooking(
                api.getListMyBooking(search, page = page).await()
            )
        )
    }

    suspend fun getBookingById(id: Int) = flow {
        emit(
            factory.createBooking(
                api.getBookingById(id).await(), getRole().safe()
            )
        )
    }

    suspend fun bookingStaff(form: BookingStaffForm) = flow {
        emit(
            api.bookStaff(form).await().id
        )
    }


    suspend fun createRecruitment(form: RecruitmentForm, salon: Salon, isShowSalon: Boolean) {
        form.validate()
        salon.validate()
        val imageParts =
            form.avatar.toScaleImagePart("image")
        api.createRecruitment(
            RequestBodyBuilder()
                .put("title", form.title)
                .put("booking_time", form.booking_time)
                .put("gender", form.gender)
                .put("experience_years", form.experience_years)
                .put("description", form.description)
                .put("address", form.address)
                .put("state", form.state)
                .put("city", form.city)
                .put("latitude", form.latitude)
                .put("longitude", form.longitude)
                .put("salary_by_skill", form.salary_by_skill)
                .put("salary_by_time", form.salary_by_time)
                .put("salary_time_values", form.salary_time_values)
                .put("contact_name", form.contact_name)
                .put("contact_phone", form.contact_phone)
                .put("salon_id", form.salon_id)
                .putIf(form.zipcode.isNotEmpty(), "zipcode", form.zipcode)
                .buildMultipart(), imageParts
        ).await()
    }

    suspend fun getAllMyRecruitment(page: Int = 1) = flow {
        emit(api.getAllMyRecruitment(page).await())
    }

    suspend fun getRecruitmentById(id: Int) = flow {
        emit(api.getDetailRecruitmentById(id = id).await())
    }

    suspend fun getAllRecruitment(page: Int = 1) = flow {
        emit(api.getAllRecruitment(page).await())
    }
    fun getRole() =
        userDataSource.get<AppConfig.AppRole>(SharePrefKey.APP_ROLE)
}