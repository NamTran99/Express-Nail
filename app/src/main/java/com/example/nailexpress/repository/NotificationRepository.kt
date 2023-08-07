package com.example.nailexpress.repository

import com.example.nailexpress.datasource.remote.NotificationApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(private val repository: NotificationApi) {
    suspend fun getListMyNotificationStaff(page: Int) = flow {
        emit(repository.getListNotificationStaff(page).await())
    }
}