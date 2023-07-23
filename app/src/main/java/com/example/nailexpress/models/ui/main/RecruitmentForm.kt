package com.example.nailexpress.models.ui.main

import android.content.Context
import com.example.nailexpress.R
import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.example.nailexpress.extension.safe
import com.example.nailexpress.extension.toDateUTC
import com.example.nailexpress.factory.TextFormatter

class RecruitmentForm(
    var avatar: String = "",
    var title: String = "",
    var gender: Int = 0, //0: fe
    var experience_years: String = "",
    var description: String = "",
    var address: String = "",
    var state: String = "",
    var city: String = "",
    var zipcode: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var contact_name: String = "",
    var contact_phone: String = "",
    var salon_id: Int? = null,
    var salary_by_skill: String? = null,
    var salary_by_time: String? = null,
    var booking_time: String? = null,
    var salary_time_values: String? = null,
    var image_url: String? = null,
    var distance: Float? = null,
    var recruitment_status: Int? = 1,
    var salary_type: Int? = null,
    var price: Double? = null,
    var unit: Int? = null,

    //custom
    @Transient
    var date: String = "",
    @Transient
    var time: String = "",
    @Transient
    var listBookTime: MutableList<BookServiceForm> = mutableListOf(),
    @Transient
    var listBookSkill: MutableList<BookServiceForm> = mutableListOf(),
    @Transient var isSelectBookingService: Boolean = true,
    var isVisibleRecycler: Boolean = false,
    @Transient
    var bookTime: BookingTime = BookingTime(),
    @Transient
    var isSkillEmpty: Boolean = true,
    @Transient
    var isTimeSkillEmpty: Boolean = true,
) : Form {
    override fun validate() {
        handleData()
    }

    fun distanceSafe() = distance?.toString() ?: ""

    fun getRecruitmentStatus() = when (recruitment_status) {
        1 -> R.string.lbl_new
        2 -> R.string.lbl_there_are_applicants
        else -> R.string.lbl_expired
    }

    fun getColorRecruitmentStatus() = when (recruitment_status) {
        1 -> R.color.color_second
        2 -> R.color.color_0B7FF4
        else -> R.color.text_content_default
    }

    fun getSalaryType(ctx: Context) = when(salary_type){
        0 -> ctx.getString(R.string.lbl_flow_service_or,TextFormatter(ctx).displaySalary(price.safe(),unit.safe()))
        1-> ctx.getString(R.string.lbl_flow_service)
        else -> TextFormatter(ctx).displaySalary(price.safe(),unit.safe())
    }

    fun saveItem(item: BookServiceForm) {
        if (isSelectBookingService) {
            item.unit = null
            listBookSkill.add(item)
            isSkillEmpty = listBookSkill.isEmpty()
        } else {
            listBookTime.add(item)
            isTimeSkillEmpty = listBookTime.isEmpty()
        }
    }

    fun removeItem(item: BookServiceForm) {
        if (isSelectBookingService) {
            listBookSkill.remove(item)
            isSkillEmpty = listBookSkill.isEmpty()
        } else {
            listBookTime.remove(item)
            isTimeSkillEmpty = listBookTime.isEmpty()
        }
    }


    override fun handleData() {
        booking_time = if (time.isNotEmpty() && date.isNotEmpty()) {
            "$date $time".toDateUTC()
        } else {
            null
        }

        salary_time_values = if (!isTimeSkillEmpty) {
            bookTime.handleData()
            bookTime.toString()
        } else {
            null
        }

        salary_by_skill = if (listBookSkill.isNotEmpty()) listBookSkill.toString() else null
        salary_by_time = if (listBookTime.isNotEmpty()) listBookTime.toString() else null
        contact_phone = contact_phone.convertPhoneToNormalFormat()
    }
}