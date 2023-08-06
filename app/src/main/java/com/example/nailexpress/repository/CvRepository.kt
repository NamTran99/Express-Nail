package com.example.nailexpress.repository

import android.content.Context
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.datasource.remote.CvApi
import com.example.nailexpress.extension.*
import com.example.nailexpress.factory.BookingCvFactory
import com.example.nailexpress.helper.RequestBodyBuilder
import com.example.nailexpress.models.ui.main.CvForm
import kotlinx.coroutines.flow.flow

class CvRepository(
    val userDataSource: SharePrefs,
    context: Context,
    val cvApi: CvApi,
    val cvFactory: BookingCvFactory
) {
    suspend fun getListCv(search: String = "", page: Int = 1) = flow {
        emit(
            cvFactory.createListCv(
                cvApi.getListCv(search, page = page).await()
            )
        )
    }

    suspend fun getCvDetail(id: Int) = flow {
        emit(
            cvFactory.createCvFactory(
                cvApi.getCvById(id).await()
            )
        )
    }

    suspend fun getListCvStaff(page: Int) = flow {
        emit(cvApi.getListCvStaff(page).await())
    }

    suspend fun getAllMyCv() = flow {
        emit(cvApi.getAllMyCv().await())
    }

    suspend fun createCv(form: CvForm) {
        form.validate()
        val avatar = form.avatar.toScaleImagePart("avatar")
        val moreImage = form.moreImage.filterRemoteImage().toArrayPart("images[]")
        cvApi.createCv(
            RequestBodyBuilder()
                .put("fullname", form.fullName)
                .put("phone", form.phone.convertPhoneToNormalFormat())
                .put("gender", form.gender)
                .put("experience_years", form.experience_years)
                .put("description", form.description)
                .put("state", form.workingAreaForm.stateSearch)
                .put("city", form.workingAreaForm.citySearch)
                .putIf(form.isSkillByTime,"salary_by_time", form.listBookTime.toString())
                .putIf(form.isSkillByService,"salary_by_skill", form.listBookSkill.toString())
                .putIf(form.isSkillByTime,"salary_time_values", form.bookTime.toString())
                .buildMultipart(),
            avatar,moreImage
        ).await()
    }
}