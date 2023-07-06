package com.example.nailexpress.models.response

data class SkillDTO(
    val curriculum_vitae_id: Int,
    val custom_skill: String,
    val id: Int,
    val name: String,
    val price: Double,
    val skill_id: Int,
    val type: Int
)