package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.bumptech.glide.Glide.init
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.databinding.LayoutCircleImageWithProgressBinding
import com.example.nailexpress.extension.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.example.nailexpress.app.AppConfig.Status


@BindingMethods(
    BindingMethod(
        type = LoadingCircleImage::class,
        attribute = "setUrl",
        method = "setImageUrl"
    ),
    BindingMethod(
        type = LoadingCircleImage::class,
        attribute = "setStatus",
        method = "setStatus"
    )
)
class LoadingCircleImage(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private val binding =
        LayoutCircleImageWithProgressBinding.inflate(LayoutInflater.from(context), this, true)

    var status = AppConfig.Status.READ
        set(value) {
            field = value
            binding.apply {
                val isUpdate = status == AppConfig.Status.UPDATE
                imgCamera.show(isUpdate)
            }
        }


     private var url : String? = ""
        set(value) {
            binding.apply {
                imgImage.setImageURICustom(value)
            }
            field = value
        }
    init {
        context.loadAttrs(attributeSet, R.styleable.LoadingCircleImage){
            status =
                AppConfig.Status.getDayByIndex(
                    it.getInt(
                        R.styleable.LoadingCircleImage_status_circle,
                        AppConfig.Status.READ.inx
                    )
                )
        }
        initView()
    }

    private fun initView() {
        with(binding) {

        }
    }

    fun setImageUrl(link: String?) {
        link?.let{
            url = it
        }
    }
}

