package com.example.nailexpress.datasource.remote

import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.helper.network.ApiAsync
import com.example.nailexpress.models.response.ServiceDTO
import retrofit2.http.*

interface GeneralApi {

    @GET("skill/list")
    fun getListService(@Query("page") page: Int, @Query("per_page") perPage: Int = AppConfig.perPage, @Query("search") search : String): ApiAsync<List<ServiceDTO>>
}