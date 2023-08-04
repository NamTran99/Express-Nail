package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.example.nailexpress.R
import com.example.nailexpress.databinding.TopBarMenuBinding
import com.example.nailexpress.utils.Constant

private typealias OnBackPress = () -> Unit
private typealias OnClickOption = () -> Unit

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

    var isShowOptionMenu: Boolean = true
        set(value) {
            binding.btnOption.isInvisible = value.not()
            field = value
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.TopBarMenu, 0, 0).run {
            try {
                val title = getString(R.styleable.TopBarMenu_tbm_title) ?: Constant.EMPTY
                val isShowOptionMenu = getBoolean(R.styleable.TopBarMenu_tbm_isShowOptionMenu, true)
                val icEnd = getDrawable(R.styleable.TopBarMenu_tbm_icEnd)
                val isShowBack = getBoolean(R.styleable.TopBarMenu_tbm_isShowBack,true)
                val paddingIcEnd = getDimension(R.styleable.TopBarMenu_tbm_padding_ic_end,14f)

                with(binding) {
                    textView.text = title
                    btnOption.isInvisible = isShowOptionMenu.not()

                    with(btnOption){
                        icEnd?.let {
                            setImageDrawable(it)
                        }

                        if (btnOption.isVisible) {
                            setOnClickListener { onClickOption?.invoke() }
                        }

                        setPadding(paddingIcEnd.toInt())
                    }

                    btnBack.isInvisible = isShowBack.not()
                    if(btnBack.isVisible){
                        btnBack.setOnClickListener {
                            onBackPress?.invoke()
                        }
                    }
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