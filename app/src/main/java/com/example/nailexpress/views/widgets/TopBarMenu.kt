package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nailexpress.R
import com.example.nailexpress.databinding.TopBarMenuBinding
import com.example.nailexpress.utils.Constant

private typealias OnBackPress = () -> Unit
private typealias OnClickOption = (optionType: OptionType) -> Unit

class TopBarMenu @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = TopBarMenuBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var onBackPress: OnBackPress? = null
    private var onClickOption: OnClickOption? = null

    var title: String
        set(value) {
            binding.textView.text = value
        }
        get() {
            return binding.textView.text?.toString() ?: Constant.EMPTY
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.TopBarMenu, 0, 0).run {
            try {
                val title = getString(R.styleable.TopBarMenu_tbm_title) ?: Constant.EMPTY
                with(binding) {
                    btnBack.setOnClickListener {
                        onBackPress?.invoke()
                    }
                    textView.text = title
                }
            } finally {
                recycle()
            }
        }
    }

    fun setOnBackPress(onBackPress: OnBackPress) {
        this.onBackPress = onBackPress
    }

    fun setOnClickOption(onClickOption: OnClickOption) {
        this.onClickOption = onClickOption
    }
}

enum class OptionType()