package com.example.nailexpress.models.ui.main

import android.content.Context
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig.TIME_DEFAULT
import com.example.nailexpress.extension.Format.FORMAT_DATE
import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.example.nailexpress.extension.getDateCurrent
import com.example.nailexpress.extension.safe
import com.example.nailexpress.extension.toDateUTC
import com.example.nailexpress.factory.TextFormatter
import com.example.nailexpress.utils.Validator.GenderDefault
import com.example.nailexpress.utils.Validator.checkEmpty
import com.example.nailexpress.utils.Validator.checkEqualDefault

class RecruitmentForm(
    var user_id: Int? = null,
    var avatar: String = "",
    var title: String = "",
    var gender: Int = GenderDefault, //0: fe
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
    var id: Int? = null,

    //custom
    @Transient
    var date: String = getDateCurrent(FORMAT_DATE),
    @Transient
    var time: String = TIME_DEFAULT,
    @Transient
    var listBookTime: MutableList<BookServiceForm> = mutableListOf(),
    @Transient
    var listBookSkill: MutableList<BookServiceForm> = mutableListOf(),
    @Transient var isSelectBookingService: Boolean = true,
    var isVisibleRecycler: Boolean = false,
    @Transient
    var salaryTimeValue: BookingTime = BookingTime(),
    @Transient
    var isSkillEmpty: Boolean = true,
    @Transient
    var isTimeSkillEmpty: Boolean = true,
    @Transient
    var isShowSalon: Boolean = false,
) : Form {
    override fun validate() {
        handleData()
        checkEmpty(title, R.string.error_empty_title, R.id.etTitle)
        checkEmpty(address, R.string.error_empty_address, R.id.etAddress)
        checkEqualDefault(gender, GenderDefault, R.string.error_select_gender)
        checkEmpty(experience_years, R.string.error_empty_experience, R.id.etYearExist)
        checkEqualDefault(listBookSkill.size +listBookTime.size, 0, R.string.error_skill_empty)
        checkEmpty(contact_name, R.string.error_empty_customer_name, R.id.etCustomerName)
        checkEmpty(contact_phone, R.string.error_empty_customer_phone, R.id.etCustomerPhone)
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

    private fun handleBookingTime(){
        booking_time = "$date $time".toDateUTC()
    }

    override fun handleData() {
        handleBookingTime()

        salary_time_values = if (!isTimeSkillEmpty) {
            salaryTimeValue.handleData()
            salaryTimeValue.toString()
        } else {
            null
        }

        salary_by_skill = if (listBookSkill.isNotEmpty()) listBookSkill.toString() else null
        salary_by_time = if (listBookTime.isNotEmpty()) listBookTime.toString() else null
        contact_phone = contact_phone.convertPhoneToNormalFormat()
    }
}