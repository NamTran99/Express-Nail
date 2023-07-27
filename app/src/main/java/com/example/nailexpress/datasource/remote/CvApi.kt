package com.example.nailexpress.datasource.remote

import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.helper.network.ApiAsync
import com.example.nailexpress.helper.network.Async
import com.example.nailexpress.models.response.CvDTO
import com.example.nailexpress.models.response.UserDTO
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface CvApi {

    @GET("cv/list")
    fun getListCv(
        @Query("search") search: String = "",
        @Query("num_per_page") perPage: Int = AppConfig.perPage,
        @Query("page") page: Int = 1,
    ): ApiAsync<List<CvDTO>>


    @GET("cv/{id}")
    fun getCvById(
        @Path("id") cvId: Int,
    ): ApiAsync<CvDTO>

    @GET("cv/list")
    fun getListCvStaff(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = AppConfig.perPage,
        @Query("latitude") latitude: String = "32.69161982273687",
        @Query("longitude") longitude: String = "-97.10703660948698"
    ): ApiAsync<List<CvDTO>>
}