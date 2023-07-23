package com.example.nailexpress.datasource.remote

import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.helper.network.ApiAsync
import com.example.nailexpress.models.response.BookingDTO
import com.example.nailexpress.models.ui.main.BookingStaffForm
import com.example.nailexpress.models.ui.main.RecruitmentForm
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RecruitmentBookingStaffApi {

    @GET("booking/my-booking")
    fun getListMyBooking(
        @Query("search") search: String = "",
        @Query("num_per_page") perPage: Int = AppConfig.perPage,
        @Query("page") page: Int = 1,
    ): ApiAsync<List<BookingDTO>>

    @GET("booking/{id}")
    fun getBookingById(
        @Path("id") id: Int,
    ): ApiAsync<BookingDTO>

    @GET("recruitment/my-recruitment")
    fun getMyRecruitment(
        @Query("num_per_page") perPage: Int = AppConfig.perPage,
        @Query("page") page: Int = 1,
    ): ApiAsync<List<BookingDTO>>

    @POST("booking/create")
    fun bookStaff(
        @Body form: BookingStaffForm
    ): ApiAsync<BookingDTO>

    @Multipart
    @POST("recruitment/create")
    fun createRecruitment(
        @PartMap buildMultipart: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: MultipartBody.Part?,
    ): ApiAsync<Any>

    @GET("recruitment/my-recruitment")
    fun getAllMyRecruitment(
        @Query("page") page: Int, @Query("per_page") perPage: Int = AppConfig.perPage
    ) : ApiAsync<List<RecruitmentForm>>
}