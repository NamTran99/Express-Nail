package com.example.nailexpress.models.response

data class UserCV(
    val address: String,
    val avatar_url: String,
    val birthday: Any,
    val city: String,
    val email: String,
    val fullname: String,
    val gender: Int,
    val id: Int,
    val language: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String,
    val phone_code: String,
    val role: String,
    val state: String,
    val state_code: String,
    val zipcode: String
)