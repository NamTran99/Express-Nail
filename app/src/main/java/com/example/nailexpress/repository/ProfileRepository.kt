package com.example.nailexpress.repository

import android.content.Context
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.datasource.remote.ProfileApi
import com.example.nailexpress.extension.buildMultipart
import com.example.nailexpress.extension.filterRemoteImage
import com.example.nailexpress.extension.toArrayPart
import com.example.nailexpress.extension.toScaleImagePart
import com.example.nailexpress.factory.ProfileFactory
import com.example.nailexpress.helper.RequestBodyBuilder
import com.example.nailexpress.models.response.UserDTO
import com.example.nailexpress.models.ui.main.PasswordForm
import com.example.nailexpress.models.ui.main.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ProfileRepository(
    val userDataSource: SharePrefs,
    val context: Context,
    val profileApi: ProfileApi,
    val profileFactory: ProfileFactory
) {

    suspend fun changePass(passwordForm: PasswordForm){
        passwordForm.validate()
        profileApi.changePassword(passwordForm).await()
    }

    // đổi role thành công trả về true
    suspend fun selectRole(appRole: AppConfig.AppRole) : Boolean{
        profileApi.selectRole(appRole.id).await()
        userDataSource.put(SharePrefKey.APP_ROLE, appRole)
        return true
    }

    suspend fun getProfile()= flow{
        profileFactory.createAUser(profileApi.getMyProfile().await()).apply {
            userDataSource.put(SharePrefKey.USER_DTO, this)
            emit(this)
        }
    }

    suspend fun updateProfile(form: User) {
        form.validate()
        val imageParts =
            form.avatar_url.toScaleImagePart("avatar")
        profileApi.updateProfile(
            RequestBodyBuilder()
                .put("fullname", form.fullname)
                .put("email", form.email)
                .put("address", form.address)
                .put("latitude", form.latitude)
                .put("longitude", form.longitude)
                .put("city", form.city)
                .put("state", form.state)
                .putIf(form.zipcode.isNotEmpty(),"zipcode", form.zipcode)
                .buildMultipart(), imageParts
        ).await()
    }

    fun getRole() =
        userDataSource.get<AppConfig.AppRole>(SharePrefKey.APP_ROLE)

    fun getProfileDTO() =
        userDataSource.get<User>(SharePrefKey.USER_DTO)
}