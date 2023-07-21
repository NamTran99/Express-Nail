package com.example.nailexpress.models.ui.main


data class Skill(
    var id: Int = 0,
    var name: String = "",
    var price: Double = 0.0,
    //custom
    var price_display: String = "",
    var isSKill: Boolean = true,
)