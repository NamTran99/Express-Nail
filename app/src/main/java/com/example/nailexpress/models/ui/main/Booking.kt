package com.example.nailexpress.models.ui.main

import android.content.Context
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.app.BookingStatus
import com.example.nailexpress.factory.TextFormatter

class Booking(
    val bookingID: Int = 0,
    val salon_id: Int = 0,
    val cv: Cv,
    val contact_name: String,
    val contact_phone: String,
    var listSkill: List<Skill> = listOf(),
    var bookingStatus: Int = BookingStatus.Pending,
    var appRole: AppConfig.AppRole = AppConfig.AppRole.Customer,
    //custom
    val status_booking_display: String = "",
    val bookingIDDisplay: String = "",
    val colorStatus: Int = R.color.color_primary,
    val displayTimeOrder: String,
    val workTime: Int? = null,
    val price: Double? = null,
    val unit: Int? = null,
    val bookingTime: String? = null,
    val state: String? = null,
    val city: String? = null,
    val createTime: String? = null
){
    fun getTimeWorkingNow(ctx: Context) : String{
        if(bookingTime == null) return ctx.getString(R.string.lbl_need_worker_now)

        return TextFormatter(ctx).displayBookingTime(bookingTime,AppConfig.AppRole.Customer)
    }
}