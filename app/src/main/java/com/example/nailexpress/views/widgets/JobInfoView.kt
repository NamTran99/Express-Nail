package com.example.nailexpress.views.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.nailexpress.R
import com.example.nailexpress.app.SalaryType
import com.example.nailexpress.databinding.LayoutJobInfoBinding
import com.example.nailexpress.extension.Format.FORMAT_DATE_DISPLAY
import com.example.nailexpress.extension.convertUTCToLocal
import com.example.nailexpress.extension.openMap
import com.example.nailexpress.factory.TextFormatter
import com.example.nailexpress.models.response.SkillDTO

class JobInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = LayoutJobInfoBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    val textFormatter  = TextFormatter(context)
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
            binding.tvMap.apply {
                isVisible = nameLocation.isNullOrBlank().not()
                setOnClickListener {
                    if (nameLocation != null) {
                        context.openMap(nameLocation)
                    }
                }
            }
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

    fun setSalary(icon: Drawable?, title: String? = null, salaryType: Int?,price: Double?, unit: Int?) {
        binding.itemSalary.apply {
            iilIcon = icon
            iilTitle = title
            iilValue =  TextFormatter(context).displaySalaryType(salaryType,price, unit)
        }
    }
    fun setTime(icon: Drawable?, title: String?, value: String?) {
        binding.itemTime.apply {
            iilIcon = icon
            iilTitle = title
            setTimeValue(value?.convertUTCToLocal(formatOutput = FORMAT_DATE_DISPLAY))
        }
    }

    fun setTimeValue(value: String?) {
        if (value != null) {
            binding.itemTime.iilValue = value
        }
    }

    fun setListService(salaryType: Int, list: List<SkillDTO>?) {
        binding.servicePriceView.apply {
            setListService(salaryType, list)
        }
    }

    fun setTimeTitle(salaryType: Int, price: Double, unit: Int){
        binding.servicePriceView.apply {
            if(SalaryType.getSalaryType(salaryType) != SalaryType.Service){
                setTimeTitle(textFormatter.displaySalary(price,unit))
            }
        }
    }
}