package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutSalonDescriptionViewBinding

class SalonDescriptionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = LayoutSalonDescriptionViewBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    var title: String? = null
        set(value) {
            value?.let {
                binding.tvTitle.text = it
            }
            field = value
        }
        get() = binding.tvTitle.text?.toString()

    var description: String? = null
        set(value) {
            isVisible = value.isNullOrBlank().not()
            value?.let {
                binding.tvDescription.text = it
            }
            field = value
        }
        get() = binding.tvDescription.text?.toString()

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.SalonDescriptionView, 0, 0).run {
            try {
                with(binding) {
                    val title = getString(R.styleable.SalonDescriptionView_sd_title)
                    val description = getString(R.styleable.SalonDescriptionView_sd_description)

                    title?.let {
                        tvTitle.text = it
                    }

                    description?.let {
                        tvDescription.text = it
                    }
                }
            } finally {
                recycle()
            }
        }
    }
}