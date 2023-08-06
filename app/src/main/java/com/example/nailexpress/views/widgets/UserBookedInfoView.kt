package com.example.nailexpress.views.widgets

import android.content.Context
import android.support.core.view.ILoadMoreAction
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingMethod
import com.example.nailexpress.R
import com.example.nailexpress.app.AppSettings
import com.example.nailexpress.app.BookingStatusDefine
import com.example.nailexpress.databinding.LayoutUserBookedInfoBinding
import com.example.nailexpress.extension.setDrawableStartTint
import com.example.nailexpress.factory.TextFormatter
import com.example.nailexpress.factory.statusBookingGetColorRes
import com.example.nailexpress.factory.statusBookingGetStringRes
import com.example.nailexpress.views.ui.main.staff.adapter.IBookingOfMeAction

class UserBookedInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding = LayoutUserBookedInfoBinding.inflate(
        LayoutInflater.from(context), this, true
    )
    private val textFormatter by lazy {
        TextFormatter(context)
    }

    private val appSettings by lazy { AppSettings(context) }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.UserBookedInfoView, 0, 0).run {
            try {
            } finally {
                recycle()
            }
        }
    }

    fun setShowExpireTime(isShow: Boolean?) {
        binding.tvExpireTime.isVisible = isShow == true
    }

    fun setID(id: Int?) {
        binding.tvId.text = context.getString(R.string.id_format, id?.toString())
    }

    fun setStatusNow(bookingTime: String?) {
        binding.statusNow.isVisible = !bookingTime.isNullOrBlank()
    }

    fun setStatusBooking(status: Int?) {
        if (status != null) {
            binding.statusBooking.apply {
                text = context.getString(status.statusBookingGetStringRes())
                setTextColor(context.getColor(status.statusBookingGetColorRes()))
                setDrawableStartTint(status.statusBookingGetColorRes())
            }
        }
    }

    fun setName(name: String?) {
        binding.tvName.text = name
    }

    fun setPhoneNumber(phone: String?) {
        binding.tvPhoneNumber.apply {
            isVisible = phone.isNullOrBlank().not()
            text = phone
        }
    }

    fun setLocation(location: String?) {
        binding.tvLocation.apply {
            isVisible = location.isNullOrBlank().not()
            text = location
        }
    }

    fun setDistance(distance: Double?) {
        binding.tvDistance.isVisible = distance != null
        if (distance != null) {
            binding.tvDistance.text = textFormatter.displayDistance(distance)
        }
    }
}