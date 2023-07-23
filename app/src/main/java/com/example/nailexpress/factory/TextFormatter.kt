package com.example.nailexpress.factory

import android.content.Context
import com.example.nailexpress.R
import com.example.nailexpress.app.Gender
import com.example.nailexpress.app.PriceUnit.DAY
import com.example.nailexpress.app.PriceUnit.HOUR
import com.example.nailexpress.app.PriceUnit.MONTH
import com.example.nailexpress.app.PriceUnit.WEEK
import com.example.nailexpress.app.PriceUnit.YEAR
import com.example.nailexpress.app.WorkType
import com.example.nailexpress.extension.formatAmount
import com.example.nailexpress.extension.formatPrice
import com.example.nailexpress.extension.or1

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

}