package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutStatusOfWorkersBinding
import com.example.nailexpress.factory.TextFormatter

private typealias OnClickViewAllWorkersApplied = () -> Unit

class StatusOfWorkersView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = LayoutStatusOfWorkersBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    private var onClickViewAllWorkersApplied: OnClickViewAllWorkersApplied? = null

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.StatusOfWorkersView, 0, 0).run {
            try {
                with(binding) {
                    btnViewAllWorkerApplied.setOnClickListener {
                        onClickViewAllWorkersApplied?.invoke()
                    }
                }
            } finally {
                recycle()
            }
        }
    }

    fun setStatusMessage(status: String?) {
        binding.tvMessageStatus.text = status
    }

    fun setListWorkerApplied(data: Int?) {
        if (data == null) return
        with(binding) {
            tvWorkersApplied.text = TextFormatter(context).displayWorkersNumber(data)
            btnViewAllWorkerApplied.apply {
                val isEnable = data > 0
                isEnabled = isEnable
                alpha = if (isEnable) 1f else 0.5f
            }
        }
    }

    fun setOnClickViewAllWorkerApplied(onClickViewAllWorkersApplied: OnClickViewAllWorkersApplied) {
        this.onClickViewAllWorkersApplied = onClickViewAllWorkersApplied
    }
}