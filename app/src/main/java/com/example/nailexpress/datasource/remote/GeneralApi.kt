package com.example.nailexpress.datasource.remote

import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.helper.network.ApiAsync
import com.example.nailexpress.models.response.SkillDTO
import com.example.nailexpress.models.ui.main.CityDTO
import com.example.nailexpress.models.ui.main.StateDTO
import retrofit2.http.*

interface GeneralApi {

    @GET("skill/list")
    fun getListService(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = AppConfig.perPage,
        @Query("search") search: String
    ): ApiAsync<List<SkillDTO>>

    @GET("state/list")
    fun getListState(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = -1, // get all
    ): ApiAsync<List<StateDTO>>

    @GET("state/cities")
    fun getListCity(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = -1, // get all
        @Query("state_code") stateCode: String,
    ): ApiAsync<List<CityDTO>>

    @GET("state/cities")
    fun getListNotification(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10, // get all
        @Query("search") search: String = "",
    ): ApiAsync<List<Any>>
}