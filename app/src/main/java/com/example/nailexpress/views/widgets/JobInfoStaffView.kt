package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutJobInfoStaffViewBinding
import com.example.nailexpress.extension.Format
import com.example.nailexpress.extension.Format.FORMAT_DATE_DISPLAY
import com.example.nailexpress.extension.convertUTCToLocal
import com.example.nailexpress.extension.setDrawableStart
import com.example.nailexpress.factory.TextFormatter

class JobInfoStaffView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding = LayoutJobInfoStaffViewBinding.inflate(
        LayoutInflater.from(context), this, true
    )
    private val textFormatter by lazy { TextFormatter(context) }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.JobInfoStaffView, 0, 0).run {
            try {
                val title =
                    getString(R.styleable.JobInfoStaffView_jis_title) ?: context.getString(R.string.job_information)

                setTitle(title)
            } finally {
                recycle()
            }
        }
    }

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setCreateTime(
        time: String?,
        inputFormatTime: String = Format.FORMAT_DATE_UTC,
        outputFormatTime: String = FORMAT_DATE_DISPLAY
    ) {
        binding.tvCreatedTime.apply {
            isVisible = time.isNullOrBlank().not()
            text = context.getString(
                R.string.ordered_time,
                time?.convertUTCToLocal(inputFormatTime, outputFormatTime)
            )
        }
    }

    fun setOrderedTime(
        time: String?
    ) {
        binding.tvBookingTime.apply {
            text = textFormatter.getTimeWorking(time)
            if (time.isNullOrBlank()) {
                setDrawableStart(R.drawable.ic_dot_5_orange)
                compoundDrawablePadding = PADDING_START_TEXT_BOOKING_TIME
                setTextColor(context.getColor(R.color.colorPrimary))
            } else {
                setTextColor(context.getColor(R.color.black))
            }
        }
    }

    fun setBookingType(salaryType: Int?,time: Int?, price: Double?, unit: Int?) {
        binding.tvBookingType.text = TextFormatter(context).displayDetailSalary(salaryType,time,price,unit)
    }

    companion object {
        private const val PADDING_START_TEXT_BOOKING_TIME = 10
    }
}