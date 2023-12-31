package com.example.nailexpress.views.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutItemInfo2LineBinding
import com.example.nailexpress.extension.safe
import com.example.nailexpress.extension.setMargins
import com.example.nailexpress.factory.TextFormatter

class ItemInfo2LineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = LayoutItemInfo2LineBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private val textFormatter by lazy {
        TextFormatter(context)
    }

    var iilIcon: Drawable? = null
        set(value) {
            binding.imgIcon.setImageDrawable(value)
            field = value
        }
        get() {
            return binding.imgIcon.drawable
        }

    var iilTitle: String? = null
        set(value) {
            binding.tvTitle.text = value
            field = value
        }
        get() {
            return binding.tvTitle.text?.toString()
        }

    var iilValue: String? = null
        set(value) {
            binding.tvValue.text = value
            field = value
        }
        get() {
            return binding.tvValue.text?.toString()
        }

    var isShowIcon: Boolean = true
        set(value) {
            binding.imgIcon.isVisible = value
            field = value
        }
        get() = binding.imgIcon.isVisible


    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ItemInfo2LineView, 0, 0).run {
            try {
                val icon = getDrawable(R.styleable.ItemInfo2LineView_iil_icon)
                val title = getString(R.styleable.ItemInfo2LineView_iil_title)
                val value = getString(R.styleable.ItemInfo2LineView_iil_value)
                val isShowIcon = getBoolean(R.styleable.ItemInfo2LineView_iil_showIcon, true)
                with(binding) {
                    imgIcon.isVisible = isShowIcon
                    icon?.let {
                        imgIcon.apply {
                            setImageDrawable(it)
                        }
                    }
                    tvTitle.text = title
                    tvValue.text = value
                }

            } finally {
                recycle()
            }
        }
    }

    fun setValueExperienceDisplay(experienceYears: Int?) {
        if (experienceYears != null) {
            binding.tvValue.text = textFormatter.displayYearExper(experienceYears)
        }
    }
}