package com.example.nailexpress.factory

import android.content.Context
import com.example.nailexpress.R
import com.example.nailexpress.app.Gender
import com.example.nailexpress.app.WorkType
import com.example.nailexpress.extension.or1

class TextFormatter(private val cxt: Context) {
    fun displayGender(gender: Int): String {
        return getString((gender == Gender.Female) or1 R.string.gender_female or2 R.string.gender_male)
    }

    fun displayDistance(distance: Double): String {
        return getString(R.string.display_distance, formatPrice(distance))
    }

    fun displayWorkType(workType: Int): String {
        return getString((workType == WorkType.NoXuyenBang) or1 R.string.no_xuyen_bang or2 R.string.xuyen_bang)
    }

    fun displayYearExper(year: Int): String {
        return getString(R.string.year_exper, year)
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


    fun formatPrice(price: Float?): String {
        return String.format("$%.2f", price).replace(",", ".")
    }

    fun formatPrice(price: Double?): String {
        return String.format("$%.2f", price).replace(",", ".")
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

}