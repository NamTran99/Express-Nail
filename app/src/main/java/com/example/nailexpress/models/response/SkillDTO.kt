package com.example.nailexpress.models.response

import com.example.nailexpress.app.SKillType

data class SkillDTO(
    val curriculum_vitae_id: Int? = null,
    val custom_skill: String? = null,
    val id: Int? = null,
    val name: String? = null,
    val price: Double? = null,
    val skill_id: Int? = null,
    val type: Int? = SKillType.Time, //  by time
)