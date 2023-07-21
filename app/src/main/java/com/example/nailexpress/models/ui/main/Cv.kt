package com.example.nailexpress.models.ui.main

class Cv(
    val name: String = "",
    val genderFormat: String = "", // vd: nam
    val genderFormat2: String = "", // vd: (Nam)
    val workingPlace: String = "", // vi tri
    val distance: Double = 0.0,
    val distanceFormat: String = "",
    val workTypeDisplay: String = "",
    val avatar: String = "",
    val id: Int = 0,
    val year_exper: Int = 0,
    val year_exper_display: String = "",// 2 năm
    val description: String = "",
    val phone: String = "",
    val listSkill: List<Skill> = listOf(),
    val state: String = "",
    val priceFormat: String = "",
    val isSkillEmpty: Boolean = false
    // custom
)
