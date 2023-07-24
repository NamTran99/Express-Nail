package com.example.nailexpress.models.response

data class SalonDTO(
    val address: String? = null,
    val city: String? = null,
    val customer_skin_color: Int? = null,
    val description: String? = "",
    val experience_years: Int? = null,
    val have_car: Int? = null,
    val have_place: Int? = null,
    val id: Int? = null,
    val images: List<ImageDTO>? = listOf(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val name: String? = null,
    val phone: String? = null,
    val state: String? = null,
    val state_code: Any? = null,
    val user_id: Int? = null,
    val zipcode: String? = null
)

