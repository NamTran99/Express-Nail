package com.example.nailexpress.models.response

data class UserDTO(
    val token: String? = "",
    val user: UserData?= UserData()
)