package com.example.nailexpress.repository

import android.content.Context
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.datasource.remote.AuthApi
import com.example.nailexpress.factory.ProfileFactory
import com.example.nailexpress.models.ui.auth.LoginForm
import com.example.nailexpress.models.ui.auth.VerifyForm

class AuthRepository(val userDataSource: SharePrefs, context: Context, val authApi: AuthApi, val profileFactory: ProfileFactory) {

    suspend fun loginAccount(loginForm: LoginForm) {
        loginForm.validate()
        profileFactory.createAUserFromDTO(authApi.loginUser(loginForm).await()).apply {
            userDataSource.put(SharePrefKey.USER_DTO, this)
            userDataSource.put(SharePrefKey.TOKEN, token)
        }
    }

    suspend fun verifyPhone(verifyPhone: VerifyForm) {
        authApi.verifyPhone(verifyPhone).await()
    }

    suspend fun verifyCode(verifyPhone: VerifyForm) {
        authApi.checkVerifyCode(verifyPhone).await()
    }

    suspend fun register(verifyPhone: VerifyForm) {
        verifyPhone.validateRegister()
        authApi.registerAccount(verifyPhone).await().apply {
            userDataSource.put(SharePrefKey.USER_DTO, this)
            userDataSource.put(SharePrefKey.TOKEN, token)
        }
    }

    suspend fun logOut() {
        authApi.logoutAccount().await()
        userDataSource.remove(SharePrefKey.TOKEN)
        userDataSource.remove(SharePrefKey.APP_ROLE)
        userDataSource.remove(SharePrefKey.USER_DTO)
    }
}