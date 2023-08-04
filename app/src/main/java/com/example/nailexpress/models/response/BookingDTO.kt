package com.example.nailexpress.models.response

import com.example.nailexpress.app.BookingStatusDefine
import com.example.nailexpress.factory.statusBookingGetStringRes
import com.example.nailexpress.factory.statusBookingGetColorRes

data class BookingDTO(
    val address: String,
    val booking_time: String? = null,
    val cancel_reason: Any,
    val canceled_by: Any,
    val city: String,
    val contact_name: String,
    val contact_phone: String,
    val created_at: String,
    val curriculum_vitae_id: Int,
    val cv: CvDTO,
    val description: String,
    val distance: Double? = null,
    val end_time: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val price: Any,
    val salary_type: Int,
    val salon_id: Int? = null,
    val staff: Staff,
    val staff_id: Int,
    val state: String,
    val state_code: Any,
    val status: Int,
    val status_text: String,
    val unit: Any,
    val updated_at: String,
    val user: UserDTO,
    val user_id: Int,
    val work_time: Int? = 0,
    val zipcode: String,
    val skills: List<SkillDTO>? = listOf()
){
    fun getStatusBookingColor() = status.statusBookingGetColorRes()

    fun getStatusBookingRes() = status.statusBookingGetStringRes()

    fun visibleMessAndCall() = status == BookingStatusDefine.Accept.bookingStatus || status == BookingStatusDefine.StartMoving.bookingStatus || status == BookingStatusDefine.Arrived.bookingStatus
}