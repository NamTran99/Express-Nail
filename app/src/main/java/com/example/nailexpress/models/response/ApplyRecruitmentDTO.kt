package com.example.nailexpress.models.response

data class ApplyRecruitmentDTO(
    val curriculum_vitae_id: Int? = null,
    val id: Int? = null,
    val recruitment_id: Int? = null,
    val status: Int? = null,
    val status_name: String? = null,
    val user: UserCV? = null,
    val user_id: Int? = null
)