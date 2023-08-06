package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.BaseActivity
import com.example.nailexpress.databinding.LayoutHeaderHomeStaffBinding
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.extension.bind
import com.example.nailexpress.extension.drawableClickRight
import com.example.nailexpress.extension.loadAttrs
import com.example.nailexpress.extension.safe

class CustomHeaderHome(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context,attributeSet) {
    val binding = LayoutHeaderHomeStaffBinding.inflate(LayoutInflater.from(context),this,true)
    private var action: IActionHeader? = null

    fun updateAction(iAction: IActionHeader){
        action = iAction
    }

    init {
        context.loadAttrs(attributeSet,R.styleable.CustomHeaderHome){ it ->
            binding.edtSearch.hint = it.getString(R.styleable.CustomHeaderHome_hint_search)
            it.getBoolean(R.styleable.CustomHeaderHome_isVisibleIcNotify,true).let {isVisibleIcNotifi->
                if(!isVisibleIcNotifi){
                    binding.tvTitle.setCompoundDrawables(null,null,null,null)
                }
            }
        }

        with(binding){
            edtSearch.bind {
                action?.onTextChange(it)
            }

            edtSearch.drawableClickRight {
                action?.onClickFilter()
            }

            tvTitle.drawableClickRight {
                action?.onClickNotification()
            }
        }
    }

    fun updateTextNotification(str: String?){
        with(binding.tvNotification){
            isVisible = !str.isNullOrBlank()
            text = str.safe()
        }
    }

    private fun getRole() =
        SharePrefs.getInstance(context).get<AppConfig.AppRole>(SharePrefKey.APP_ROLE)

    interface IActionHeader{
        fun onTextChange(string: String)
        fun onClickNotification()

        fun onClickFilter()
    }
}