package com.example.nailexpress.factory

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.text.SpannableStringBuilder
import androidx.core.text.color
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.app.BookingStatusDefine
import com.example.nailexpress.app.Gender
import com.example.nailexpress.app.PriceUnit.DAY
import com.example.nailexpress.app.PriceUnit.HOUR
import com.example.nailexpress.app.PriceUnit.MONTH
import com.example.nailexpress.app.PriceUnit.WEEK
import com.example.nailexpress.app.PriceUnit.YEAR
import com.example.nailexpress.app.WorkType
import com.example.nailexpress.extension.*
import com.example.nailexpress.extension.Format.FORMAT_DATE_DISPLAY
import java.text.SimpleDateFormat
import java.util.Locale

class TextFormatter(private val cxt: Context) {
    fun displayGender(gender: Int): String {
        return getString((gender == Gender.Female) or1 R.string.gender_female or2 R.string.gender_male)
    }

    fun displayDistance(distance: Double): String {
        return getString(R.string.display_distance, distance.formatAmount())
    }

    fun displayWorkType(workType: Int): String {
        return getString((workType == WorkType.NoXuyenBang) or1 R.string.no_xuyen_bang or2 R.string.xuyen_bang)
    }

    fun displayYearExper(year: Int): String {
        return getString(R.string.year_exper, year)
    }

    fun displayWorkersNumber(number: Int): String {
        return getString(R.string.num_of_workers, number)
    }

    fun displayBookingTime(bookingTime: String?, appRole: AppConfig.AppRole): String {
        return bookingTime?.let {
            bookingTime.convertUTCToLocal(formatOutput = FORMAT_DATE_DISPLAY)
        }
            ?: getString((appRole == AppConfig.AppRole.Customer) or1 R.string.booking_now_customer or2 R.string.booking_now_staff)
    }

    fun displaySalary(price: Double, unit: Int): String {
        return if (unit in 1..5) {
            val temp = when (unit) {
                HOUR -> R.string.time_type_1
                DAY -> R.string.time_type_2
                WEEK -> R.string.time_type_3
                MONTH -> R.string.time_type_4
                YEAR -> R.string.time_type_5
                else -> R.string.time_type_1
            }
            val suffix = getString(temp)
            "$${price}${suffix}"
        } else {
            "$${price}"
        }
    }

    fun displaySalaryType(salaryType: Int?,price: Double, unit: Int?): String{
        return when(salaryType){
            0 -> getString(R.string.lbl_flow_service_or,TextFormatter(cxt).displaySalary(price.safe(),unit.safe()))
            1-> getString(R.string.lbl_flow_service)
            else -> TextFormatter(cxt).displaySalary(price.safe(),unit.safe())
        }
    }

    //theo gio 4 gio x $50/gio
    fun displayDetailSalary(salaryType: Int?,time: Int?, price: Double?, unit: Int?): CharSequence {
        if(salaryType == 1) return getString(R.string.book_type_1)
        return if (unit in 1..5) {
            val temp = when (unit) {
                HOUR -> R.string.time_type_1
                DAY -> R.string.time_type_2
                WEEK -> R.string.time_type_3
                MONTH -> R.string.time_type_4
                YEAR -> R.string.time_type_5
                else -> R.string.time_type_1
            }
            val suffix = getString(temp)

            SpannableStringBuilder().apply {
                append(cxt.getString(R.string.lbl_flow)," ",convertDisplayUnit(unit)," ")
                color(cxt.getColor(R.color.color_primary)){
                    append(time?.toString() ?: " "," ",convertDisplayUnit(unit)," x ")
                    append("$${price}${suffix}")
                }
            }
        } else {
            "$ $price"
        }
    }

    private fun convertDisplayUnit(unit: Int?) = getString(when(unit){
        HOUR -> R.string.time_type_1_1
        DAY -> R.string.time_type_2_2
        WEEK -> R.string.time_type_3_3
        MONTH -> R.string.time_type_4_4
        YEAR -> R.string.time_type_5_5
        else -> R.string.time_type_1_1
    })

    fun displayYesNo(inx: Int): String {
        return getString(
            when (inx) {
                0 -> R.string.no
                1 -> R.string.yes
                else -> R.string.no
            }
        )
    }

    fun customerSkinColorFormat(indx: Int): String {
        return getString(
            when (indx) {
                0 -> R.string.skin_all
                1 -> R.string.skin_white
                2 -> R.string.skin_black
                3 -> R.string.skin_xi
                else -> R.string.skin_white
            }
        )
    }

    fun displayStatusBooking(status: Int): String {
        return getString(
            when (status) {
                0 -> R.string.status_booking_0
                1 -> R.string.status_booking_1
                2 -> R.string.status_booking_2
                else -> R.string.status_booking_0
            }
        )
    }

    fun displayStatusColor(status: Int): Int {
        return when (status) {
            0 -> R.color.color_primary
            1 -> R.color.color_0B7FF4
            2 -> R.color.color_ED1B1B
            else -> R.color.color_primary
        }
    }


    fun getString(value: Int): String {
        return cxt.getString(value)
    }

    fun getString(value: Int, params: Any): String {
        return cxt.getString(value, params)
    }

    fun getString(value: Int, vararg data: Any): String {
        return cxt.getString(value, *data)
    }

    fun getStatusRecruitment(status: Int): String {
        return when (status) {
            1 -> cxt.getString(R.string.lbl_new)
            2 -> cxt.getString(R.string.lbl_there_are_applicants)
            else -> cxt.getString(R.string.lbl_expired)
        }
    }

    fun getTimeWorking(time: String?) = time?.convertUTCToLocal(formatOutput = FORMAT_DATE_DISPLAY) ?: getString(R.string.lbl_need_you_to_working)

    fun formatPhoneUS(str: String?): String {
        return if (str.isNullOrEmpty()) ""
        else PhoneNumberUtils.formatNumber(str, "US")
    }

    fun formatDate(inputDate: String?): String {
        if (inputDate == null) return ""
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
        val outputDateFormat = SimpleDateFormat("hh:mm a - dd/MM/yyyy", Locale.US)

        return try {
            val date = inputDateFormat.parse(inputDate)
            getString(R.string.lbl_booking,outputDateFormat.format(date))
        } catch (e: Exception) {
            inputDate
        }
    }
}

fun Int.statusBookingGetColorRes() =
    kotlin.run { BookingStatusDefine.values().first { it.bookingStatus == this }.color }

fun Int.statusBookingGetStringRes() =
    run { BookingStatusDefine.values().first { it.bookingStatus == this }.bookingStringRes }