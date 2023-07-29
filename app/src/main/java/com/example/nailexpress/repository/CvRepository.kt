package com.example.nailexpress.repository

import android.content.Context
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.datasource.remote.CvApi
import com.example.nailexpress.factory.BookingCvFactory
import kotlinx.coroutines.flow.flow
import retrofit2.http.Path

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
}