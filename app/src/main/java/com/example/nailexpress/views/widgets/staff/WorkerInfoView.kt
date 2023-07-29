package com.example.nailexpress.views.widgets.staff

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutWorkerInfoBinding

class WorkerInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding = LayoutWorkerInfoBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.WorkerInfoView, 0, 0).run {
            try {
                val title = getString(R.styleable.WorkerInfoView_wiv_title)
                val iconTopLeft = getDrawable(R.styleable.WorkerInfoView_wiv_iconTopLeft)
                val iconTopRight = getDrawable(R.styleable.WorkerInfoView_wiv_iconTopRight)
                val iconBotLeft = getDrawable(R.styleable.WorkerInfoView_wiv_iconBotLeft)
                val iconBotRight = getDrawable(R.styleable.WorkerInfoView_wiv_iconBotRight)
                val titleTopLeft = getString(R.styleable.WorkerInfoView_wiv_titleTopLeft)
                val titleTopRight = getString(R.styleable.WorkerInfoView_wiv_titleTopRight)
                val titleBotLeft = getString(R.styleable.WorkerInfoView_wiv_titleBotLeft)
                val titleBotRight = getString(R.styleable.WorkerInfoView_wiv_titleBotRight)

                setTitle(title)
                setIconTopLeft(iconTopLeft)
                setIconTopRight(iconTopRight)
                setIconBotLeft(iconBotLeft)
                setIconBotRight(iconBotRight)
                setTitleTopLeft(titleTopLeft)
                setTitleTopRight(titleTopRight)
                setTitleBotLeft(titleBotLeft)
                setTitleBotRight(titleBotRight)
            } finally {
                recycle()
            }
        }
    }

    fun setTitle(title: String?) {
        binding.titleJobInfo.apply {
            text = title
        }
    }

    fun setIconTopLeft(icon: Drawable?) {
        binding.itemTopLeft.iilIcon = icon
    }

    fun setIconTopRight(icon: Drawable?) {
        binding.itemTopRight.iilIcon = icon
    }

    fun setIconBotLeft(icon: Drawable?) {
        binding.itemBotLeft.iilIcon = icon
    }

    fun setIconBotRight(icon: Drawable?) {
        binding.itemBotRight.iilIcon = icon
    }

    fun setTitleTopLeft(title: String?) {
        binding.itemTopLeft.iilTitle = title
    }

    fun setTitleTopRight(title: String?) {
        binding.itemTopRight.iilTitle = title
    }

    fun setTitleBotLeft(title: String?) {
        binding.itemBotLeft.iilTitle = title
    }

    fun setTitleBotRight(title: String?) {
        binding.itemBotRight.iilTitle = title
    }

    fun setValueTopLeft(value: String?) {
        binding.itemTopLeft.iilValue = value
    }

    fun setValueTopRight(value: String?) {
        binding.itemTopRight.iilValue = value
    }

    fun setValueBotLeft(value: String?) {
        binding.itemBotLeft.iilValue = value
    }

    fun setValueBotRight(value: String?) {
        binding.itemBotRight.iilValue = value
    }
}