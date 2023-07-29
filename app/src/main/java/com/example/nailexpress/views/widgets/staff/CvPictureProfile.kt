package com.example.nailexpress.views.widgets.staff

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutCvPictureProfileBinding
import com.example.nailexpress.extension.setImageURICustom

class CvPictureProfile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val binding = LayoutCvPictureProfileBinding.inflate(
        LayoutInflater.from(context),
        this,
        true,
    )

    var name: String? = null
        set(value) {
            binding.tvName.apply {
                text = value
            }
            field = value
        }

    var gender: String? = null
        set(value) {
            binding.tvGender.apply {
                isVisible = value.isNullOrBlank().not()
                text = context.getString(R.string.gender_format, value)
            }
            field = value
        }

    var nickName: String? = null
        set(value) {
            binding.tvNickName.apply {
                isVisible = value.isNullOrBlank().not()
                text = value
            }
            field = value
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CvPictureProfile, 0, 0).run {
            try {
                with(binding) {}
            } finally {
                recycle()
            }
        }
    }

    fun setAvatarUrl(url: String?) {
        binding.imgAvatar.setImageURICustom(url)
    }
}