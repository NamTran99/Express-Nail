package com.example.nailexpress.datasource.remote

import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.helper.network.ApiAsync
import com.example.nailexpress.helper.network.Async
import com.example.nailexpress.models.response.CvDTO
import com.example.nailexpress.models.response.SalonDTO
import com.example.nailexpress.models.response.UserDTO
import com.example.nailexpress.models.response.UserData
import com.example.nailexpress.models.ui.main.PasswordForm
import com.example.nailexpress.models.ui.main.User
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ProfileApi {

    @FormUrlEncoded
    @POST("user/switch-role")
    fun selectRole(
        @Field("role") role: Int,
    ): ApiAsync<Any>


    @GET("user/my-profile")
    fun getMyProfile(
    ): ApiAsync<UserData>

    @Multipart
    @POST("user/update-profile")
    fun updateProfile(
        @PartMap buildMultipart: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: MultipartBody.Part?,
    ): ApiAsync<Any>

    @POST("user/change-password")
    fun changePassword(
        @Body form: PasswordForm,
    ): ApiAsync<Any>

}