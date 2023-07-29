package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseActivity
import com.example.nailexpress.databinding.LayoutCustomFilterViewBinding
import com.example.nailexpress.extension.loadAttrs

private const val TAG = "IActionCustomFilter"

class CustomFilterView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val binding =
        LayoutCustomFilterViewBinding.inflate(LayoutInflater.from(context), this, true)
    private var iAction: IActionCustomFilter? = null

    init {
        context.loadAttrs(attrs, R.styleable.CustomFilterView) {
            with(binding) {
                val isVisibleExpress =
                    it.getBoolean(R.styleable.CustomFilterView_isVisibleExpression, true)
                tvExpression.isVisible = isVisibleExpress
                tvExpressionValue.isVisible = isVisibleExpress

                val isVisibleGender =
                    it.getBoolean(R.styleable.CustomFilterView_isVisibleGender, true)
                tvGender.isVisible = isVisibleGender
                tvGenderValue.isVisible = isVisibleGender
            }
        }

        with(binding) {
            tvState.setOnClickListener {
                iAction?.onClickState()
            }

            tvCity.setOnClickListener {
                iAction?.onClickCity()
            }

            tvExpressionValue.setOnClickListener {
                iAction?.onClickExpression()
            }

            tvGenderValue.setOnClickListener {
                iAction?.onClickGender()
            }

            tvClose.setOnClickListener {
                if(context is FragmentActivity){
                    val activity = context as? BaseActivity<*>
                    activity?.navController?.popBackStack()
                }
            }
        }
    }

    fun setAction(action: IActionCustomFilter) {
        iAction = action
    }

    interface IActionCustomFilter {
        fun onClickState() {
            Log.d(TAG, "onClickState: ")
        }

        fun onClickCity() {
            Log.d(TAG, "onClickCity: ")
        }

        fun onClickExpression() {
            Log.d(TAG, "onClickExpression: ")
        }

        fun onClickGender() {
            Log.d(TAG, "onClickGender: ")
        }

        fun onClickClose(){
            Log.d(TAG, "onClickClose: ")
        }
    }
}
