package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutShopInfoViewBinding

class ShopInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = LayoutShopInfoViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var shopName: String? = null
        set(value) {
            value?.let {
                binding.shopName.text = it
            }
            field = value
        }
        get() = binding.shopName.text?.toString()

    var phoneNumber: String? = null
        set(value) {
            binding.tvPhoneNumber.apply {
                isVisible = value.isNullOrBlank().not() == true
                text = value
            }
            field = value
        }
        get() = binding.tvPhoneNumber.text?.toString()

    var location: String? = null
        set(value) {
            binding.tvLocation.apply {
                isVisible = value.isNullOrBlank().not() == true
                text = value
            }
            field = value
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ShopInfoView, 0, 0).run {
            try {
                val title = getString(R.styleable.ShopInfoView_si_title)
                with(binding) {
                    title?.let {
                        tvTitle.text = it
                    }
                }
            } finally {
                recycle()
            }
        }
    }
}