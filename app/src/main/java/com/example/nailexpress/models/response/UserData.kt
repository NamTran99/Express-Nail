package com.example.nailexpress.models.response

data class UserData(
    val address: String? = "",
    val avatar_url: String? = "",
    val birthday: String? = "",
    val city: String? = "",
    val email: String? = "",
    val fullname: String? = "",
    val gender: Int? = 0,
    val id: Int? = 0,
    val language: String? = "",
    val latitude: String? = "",
    val longitude: String? = "",
    val phone: String? = "",
    val phone_code: String? = "",
    val role: String? = "",
    val state: String? = "",
    val state_code: String? = "",
    val token: String? = "",
    val zipcode: String? = ""
)