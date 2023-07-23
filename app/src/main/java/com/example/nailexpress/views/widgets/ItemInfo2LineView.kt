package com.example.nailexpress.views.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutItemInfo2LineBinding

class ItemInfo2LineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = LayoutItemInfo2LineBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

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


    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ItemInfo2LineView, 0, 0).run {
            try {
                val icon = getDrawable(R.styleable.ItemInfo2LineView_iil_icon)
                val title = getString(R.styleable.ItemInfo2LineView_iil_title)
                val value = getString(R.styleable.ItemInfo2LineView_iil_value)
                with(binding) {
                    icon?.let {
                        imgIcon.setImageDrawable(it)
                    }
                    tvTitle.text = title
                    tvValue.text = value
                }

            } finally {
                recycle()
            }
        }
    }
}