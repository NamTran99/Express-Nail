package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutUserBookedInfoBinding
import com.example.nailexpress.factory.TextFormatter

class UserBookedInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding = LayoutUserBookedInfoBinding.inflate(
        LayoutInflater.from(context), this, true
    )
    private val textFormatter by lazy {
        TextFormatter(context)
    }

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
        binding.statusNow.isVisible = bookingTime.isNullOrBlank()
    }

    fun setStatusBooking(status: Int?) {
        if (status != null) {
            binding.statusBooking.text = status.toString()
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