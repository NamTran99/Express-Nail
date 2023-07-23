package com.example.nailexpress.views.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutJobInfoBinding
import com.example.nailexpress.factory.TextFormatter

class JobInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = LayoutJobInfoBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.JobInfoView, 0, 0).run {
            try {

            } finally {
                recycle()
            }
        }
    }

    fun setLocation(nameLocation: String?) {
        binding.tvLocation.apply {
            isVisible = nameLocation.isNullOrBlank().not() == true
            text = nameLocation
        }
    }

    fun setExperience(icon: Drawable?, title: String?, value: Int?) {
        binding.itemExp.apply {
            iilIcon = icon
            iilTitle = title
            setExperienceValue(value)
        }
    }

    fun setExperienceValue(value: Int?) {
        if (value != null) {
            binding.itemExp.iilValue = TextFormatter(context).displayYearExper(value)
        }
    }

    fun setGender(icon: Drawable?, title: String?, value: Int?) {
        binding.itemGender.apply {
            iilIcon = icon
            iilTitle = title
            setGenderValue(value)
        }
    }

    fun setGenderValue(value: Int?) {
        if (value != null) {
            binding.itemGender.iilValue = TextFormatter(context).displayGender(value)
        }
    }

    fun setSalary(icon: Drawable?, title: String?, price: Double?, unit: Int?) {
        binding.itemSalary.apply {
            iilIcon = icon
            iilTitle = title
            setSalaryValue(price, unit)
        }
    }

    fun setSalaryValue(price: Double?, unit: Int?) {
        if (price != null && unit != null) {
            binding.itemSalary.iilValue = TextFormatter(context).displaySalary(price, unit)
        }
    }

    fun setTime(icon: Drawable?, title: String?, value: String?) {
        binding.itemTime.apply {
            iilIcon = icon
            iilTitle = title
            setTimeValue(value)
        }
    }

    fun setTimeValue(value: String?) {
        if (value != null) {
            binding.itemTime.iilValue = value
        }
    }
}