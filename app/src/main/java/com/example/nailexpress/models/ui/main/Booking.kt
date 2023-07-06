package com.example.nailexpress.models.ui.main

import com.example.nailexpress.R

class Booking(
    val bookingID: Int = 0,
    val salon_id: Int = 0,
    val cv: Cv,
    val contact_name: String,
    val contact_phone: String,
    var listSkill: List<ISkill> = listOf(),
    //custom
    val status_booking_display : String= "",
    val bookingIDDisplay: String = "",
    val colorStatus: Int = R.color.color_primary
)