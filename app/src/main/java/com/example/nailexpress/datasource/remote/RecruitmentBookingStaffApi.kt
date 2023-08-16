package com.example.nailexpress.datasource.remote

import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.helper.network.ApiAsync
import com.example.nailexpress.models.response.BookingDTO
import com.example.nailexpress.models.response.RecruitmentDataDTO
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
    ): ApiAsync<RecruitmentDataDTO>

    @GET("recruitment/my-recruitment")
    fun getAllMyRecruitment(
        @Query("page") page: Int, @Query("per_page") perPage: Int = AppConfig.perPage
    ): ApiAsync<List<RecruitmentForm>>

    @GET("recruitment/{id}")
    fun getDetailRecruitmentById(
        @Path("id") id: Int
    ): ApiAsync<RecruitmentDataDTO>

    @GET("recruitment/list")
    fun getAllRecruitment(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = AppConfig.perPage,
        @Query("latitude") latitude: String = "32.69161982273687",
        @Query("longitude") longitude: String = "-97.10703660948698"
    ): ApiAsync<List<RecruitmentForm>>

    @GET("booking/booking-me")
    fun getListBookingOfMe(
        @Query("per_page") perPage: Int = AppConfig.perPage,
        @Query("page") page: Int
    ): ApiAsync<List<BookingDTO>>

    @GET("recruitment/my-apply")
    fun getListRecruitmentMyApply(
        @Query("per_page") perPage: Int = AppConfig.perPage,
        @Query("page") page: Int
    ): ApiAsync<List<RecruitmentForm>>

    @POST("booking/change-status")
    @FormUrlEncoded
    fun changeStatusBooking(
        @Field("booking_id") idBooking: Int,
        @Field("status") status: Int,
        @Field("cancel_reason") message: String?
    ): ApiAsync<Any>
}