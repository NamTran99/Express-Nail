package com.example.nailexpress.models.response

data class RecruitmentDataDTO(
    val address: String? = null,
    val applies: List<ApplyRecruitmentDTO>? = null,
    val apply_status: Any? = null,
    val booking_time: String? = null,
    val city: String? = null,
    val contact_name: String? = null,
    val contact_phone: String? = null,
    val description: String? = null,
    val distance: Double? = null,
    val experience_years: Int? = null,
    val gender: Int? = null,
    val id: Int? = null,
    val image_url: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val price: Double? = null,
    val recruitment_status: Int? = null,
    val salary_type: Int = 0,
    val salary_type_text: String? = null,
    val salon_id: Int? = null,
    val skills: List<SkillDTO>? = null,
    val state: String? = null,
    val state_code: Any? = null,
    val status: Int? = null,
    val title: String? = null,
    val unit: Int? = null,
    val unit_text: String? = null,
    val user_id: Int? = null,
    val zipcode: String? = null
)