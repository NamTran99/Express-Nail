package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutInfoUserBookWorkerViewBinding

class InfoUserBookWorkerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = LayoutInfoUserBookWorkerViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var customerName: String? = null
        set(value) {
            value?.let {
                binding.tvCustomerName.text = it
            }
            field = value
        }
        get() = binding.tvCustomerName.text?.toString()

    var phoneNumber: String? = null
        set(value) {
            value?.let {
                binding.tvPhoneNumber.text = it
            }
            field = value
        }
        get() = binding.tvPhoneNumber.text?.toString()

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.InfoUserBookWorkerView, 0, 0).run {
            try {
                val title = getString(R.styleable.InfoUserBookWorkerView_iubw_title)
                val titleName = getString(R.styleable.InfoUserBookWorkerView_iubw_titleName)
                    ?: context.getString(R.string.customer_name)

                val titlePhoneNumber = getString(R.styleable.InfoUserBookWorkerView_iubw_titlePhoneNumber)
                    ?: context.getString(R.string.phone_number)
                with(binding) {
                    title?.let {
                        tvTitle.text = it
                    }
                    setTitleName(titleName)
                    setTitlePhoneNumber(titlePhoneNumber)
                }

            } finally {
                recycle()
            }
        }
    }

    fun setTitleName(title: String) {
        binding.tvTitleCustomerName.text = title
    }

    fun setTitlePhoneNumber(title: String) {
        binding.tvTitlePhoneNumber.text = title
    }
}