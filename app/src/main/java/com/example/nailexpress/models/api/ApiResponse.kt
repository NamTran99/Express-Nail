package com.example.nailexpress.models.api

class ApiResponse<T>(
    val data: T,
    val result: Boolean,
    val message: String
)