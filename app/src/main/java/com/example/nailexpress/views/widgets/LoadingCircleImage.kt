package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.example.nailexpress.R
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

    init {
        initView()
    }
    var onClickUploadImage  ={}

    private fun initView() {
        with(binding) {
            root.onClick{
                if(status == Status.UPDATE){
                    onClickUploadImage.invoke()
                }
            }
        }
    }

    var status = Status.READ

    fun setImageUrl(link: String?) {
        with(binding) {
            imgImage.setImageURICustom(link)
        }
    }
}

