package com.example.nailexpress.models.response

import com.example.nailexpress.models.ui.main.RecruitmentForm

data class CvDTO(
    val address: String,
    val avatar_url: String,
    val birthday: Any,
    val city: String,
    val description: String,
    val distance: Double = 0.0,
    val email: Any,
    val experience_years: Int,
    val fullname: String,
    val gender: Int,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val phone: String,
    val salary_type: Int,
    val state: String,
    val state_code: Any,
    val price: Double?,
    val unit: Int?,
    val status: Int,
    val user: UserCV,
    val user_id: Int,
    val work_place: String? = "",
    val work_type: Int,
    val zipcode: String,
    val skills: List<SkillDTO>? = listOf()
)