package com.example.nailexpress.models.response

data class SalonDTO(
    val address: String,
    val city: String,
    val customer_skin_color: Int,
    val description: String? = "",
    val experience_years: Int,
    val have_car: Int,
    val have_place: Int,
    val id: Int,
    val images: List<ImageDTO>? = listOf(),
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val phone: String,
    val state: String,
    val state_code: Any,
    val user_id: Int,
    val zipcode: String,
)

