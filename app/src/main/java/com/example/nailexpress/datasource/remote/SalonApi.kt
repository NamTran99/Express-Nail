package com.example.nailexpress.datasource.remote

import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.helper.network.ApiAsync
import com.example.nailexpress.helper.network.Async
import com.example.nailexpress.models.response.CvDTO
import com.example.nailexpress.models.response.SalonDTO
import com.example.nailexpress.models.response.UserDTO
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface SalonApi {

    @Multipart
    @POST("salon/create")
    fun createSalon(
        @PartMap buildMultipart: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: Array<MultipartBody.Part?>,
    ): ApiAsync<SalonDTO>

    @GET("salon/my-salons")
    fun getMySalon(
    ): ApiAsync<List<SalonDTO>>

    @GET("salon/{id}")
    fun getSalonById(
        @Path("id") id: Int
    ): ApiAsync<SalonDTO>
}