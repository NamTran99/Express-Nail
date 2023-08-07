package com.example.nailexpress.datasource.remote

import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.helper.network.ApiAsync
import com.example.nailexpress.models.response.NotificationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationApi {
    @GET("notification/my-notification?")
    fun getListNotificationStaff(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = AppConfig.perPage,
    ): ApiAsync<List<NotificationResponse>>
}