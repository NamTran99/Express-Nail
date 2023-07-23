package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.webkit.URLUtil
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutUserInfoDetailPostBinding
import com.example.nailexpress.factory.TextFormatter

class UserInfoDetailPostView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = LayoutUserInfoDetailPostBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.UserInfoDetailPostView, 0, 0).run {
            try {

            } finally {
                recycle()
            }
        }
    }

    fun setUserImg(url: String?) {
        val isValidUrl = URLUtil.isValidUrl(url)
        binding.imgUser.isVisible = isValidUrl
        if (isValidUrl) {
            Glide.with(context)
                .load(url)
                .placeholder(AppCompatResources.getDrawable(context, R.drawable.placeholder_user_post_detail))
                .into(binding.imgUser)
        }
    }

    fun setUserId(id: Any?) {
        binding.tvId.isVisible = id != null
        id?.let {
            binding.tvId.text = "#ID :$it"
        }
    }

    fun setStatus(status: Int?) {
        binding.tvStatus.apply {
            isVisible = status != null
            status?.let {
                text = TextFormatter(context).getStatusRecruitment(status)
            }
        }
    }

    fun setContent(content: String?) {
        binding.contentHiring.apply {
            isVisible = content.isNullOrBlank().not() == true
            text = content
        }
    }

    fun setSalonName(salonName: String?) {
        binding.nameSalon.apply {
            isVisible = salonName.isNullOrBlank().not() == true
            text = salonName
        }
    }

    fun setDistance(distance: Double?) {
        binding.distance.apply {
            isVisible = distance != null
            text = TextFormatter(context).displayDistance(distance ?: 0.0)
        }
    }
}