package com.example.nailexpress.models.ui.main

import android.hardware.Camera
import com.example.nailexpress.models.ui.AppImage

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
    val isSkillEmpty: Boolean = false,
    val salaryType: Int = 0,
    val price: Double? = null,
    val unit: Int? = null
    // custom
)

data class CvForm(
    var isSkillByTime: Boolean = false,
    var isSkillByService: Boolean = false,
    var avatar: String = "",
    var gender: Int = 0,
    var fullName: String = "",
    var lastName: String = "",
    var firstName: String = "",
    var experience_years: String = "-1",
    val genderFormat: String = "", // vd: nam
    val genderFormat2: String = "", // vd: (Nam)
    val workingPlace: String = "", // vi tri
    val distance: Double = 0.0,
    val distanceFormat: String = "",
    val workTypeDisplay: String = "",
    val id: Int = 0,
    val year_exper: Int = 0,
    val year_exper_display: String = "",// 2 năm
    var description: String = "",
    var phone: String = "",
    val listSkill: List<Skill> = listOf(),
    var state: String = "",
    var city: String = "",
    val priceFormat: String = "",
    val isSkillEmpty: Boolean = false,
    val salaryType: Int = 0,
    var workingAreaForm: SearchCityStateForm = SearchCityStateForm(),
    // custom
    var listBookTime: MutableList<BookServiceForm> = mutableListOf(),
    var listBookSkill: MutableList<BookServiceForm> = mutableListOf(),
    val moreImage:  MutableList<AppImage> = mutableListOf<AppImage>(),
    var bookTime: BookingTime = BookingTime(),
): Form{
    override fun validate() {
        handleData()
    }

    fun saveItem(item: BookServiceForm) {
        if (!item.isTypeTime) {
            item.unit = null
            listBookSkill.add(item)
        } else {
            listBookTime.add(item)
        }
    }

    override fun handleData(){
        fullName = "$lastName $firstName"
    }
}
