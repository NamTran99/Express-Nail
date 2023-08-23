package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutEmptyBinding
import com.example.nailexpress.extension.safe
import com.example.nailexpress.extension.show

class LayoutEmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = LayoutEmptyBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var text: String = ""
        set(value) {
            field = value
            binding.tvEmptyData.text = value
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LayoutEmptyView, 0, 0).run {
            try {
                text = getString(R.styleable.LayoutEmptyView_custom_text).safe()
            } finally {
                recycle()
            }
        }
    }

    fun setShowView(isShow: Boolean){
        binding.lvEmpty.show(isShow)
    }
}