package com.example.nailexpress.models.ui.main

interface ISkill {
    val name: String get() =""
    val id: Int get() = 0
    val price: Double get() = 0.0
    val priceDisplay: String get() = ""
}