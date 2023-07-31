package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutJobInfoStaffViewBinding

class JobInfoStaffView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding = LayoutJobInfoStaffViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.JobInfoStaffView, 0, 0).run {
            try {

            } finally {
                recycle()
            }
        }
    }
}