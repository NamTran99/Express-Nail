package com.example.nailexpress.repository

import android.content.Context
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.datasource.remote.GeneralApi
import com.example.nailexpress.factory.GeneralFactory
import com.example.nailexpress.models.ui.main.Salon
import kotlinx.coroutines.flow.flow

class GeneralRepository(
    val userDataSource: SharePrefs,
    val context: Context,
    val generalApi: GeneralApi,
    val generalFactory: GeneralFactory
) {
    suspend fun getListService(search: String, page: Int) = flow {
        emit(
            generalFactory.createListService(
                generalApi.getListService(search = search, page = page).await()
            )
        )
    }
}