package com.example.nailexpress.datasource.remote

import com.example.nailexpress.helper.network.ApiAsync
import com.example.nailexpress.models.response.UserDTO
import com.example.nailexpress.models.ui.auth.LoginForm
import com.example.nailexpress.models.ui.auth.VerifyForm
import retrofit2.http.*

interface AuthApi {

    @POST("user/login")
    fun loginUser(@Body loginForm: LoginForm): ApiAsync<UserDTO>

    @POST("user/verify-phone")
    fun verifyPhone(@Body verifyForm: VerifyForm): ApiAsync<UserDTO>

    @POST("user/check-verify-code")
    fun checkVerifyCode(@Body verifyForm: VerifyForm): ApiAsync<UserDTO>

    @POST("user/register")
    fun registerAccount(@Body verifyForm: VerifyForm): ApiAsync<UserDTO>

    @POST("user/logout")
    fun logoutAccount(): ApiAsync<Any>
}