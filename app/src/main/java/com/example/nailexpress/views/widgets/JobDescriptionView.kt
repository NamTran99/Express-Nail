package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutJobDescriptionViewBinding
import com.example.nailexpress.extension.show

class JobDescriptionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = LayoutJobDescriptionViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var title: String? = null
        set(value) {
            binding.tvTitle.text = value
            field = value
        }
        get() = binding.tvTitle.text?.toString()

    var description: String? = null
        set(value) {
            binding.apply {
                tvContent.show(!value.isNullOrEmpty())
                tvTitle.show(!value.isNullOrEmpty())
                tvContent.text = value
            }
            field = value
        }
        get() = binding.tvContent.text?.toString()

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.JobDescriptionView, 0, 0).run {
            try {
                val title = getString(R.styleable.JobDescriptionView_jd_title)
                val content = getString(R.styleable.JobDescriptionView_jd_content)
                val titleTextColor =
                    getColor(R.styleable.JobDescriptionView_jd_titleTextColor, context.getColor(R.color.color_second))
                val contextTextColor =
                    getColor(R.styleable.JobDescriptionView_jd_contentTextColor, context.getColor(R.color.black_656565))
                with(binding) {
                    title?.let {
                        tvTitle.text = it
                    }
                    content?.let {
                        tvContent.text = it
                    }
                    setTitleTextColor(titleTextColor)
                    setDescriptionTextColor(contextTextColor)
                }
            } finally {
                recycle()
            }
        }
    }

    fun setTitleTextColor(color: Int) {
        binding.tvTitle.setTextColor(color)
    }

    fun setDescriptionTextColor(color: Int) {
        binding.tvContent.setTextColor(color)
    }
}