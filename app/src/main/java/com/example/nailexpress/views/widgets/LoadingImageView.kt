package com.example.nailexpress.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.databinding.LoadingImageBinding
import com.example.nailexpress.extension.*

class LoadingImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val binding by lazy {
        LoadingImageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    var enableRemoveImage = true
        set(value) {
            field = value
            binding.btClose.show(value && status == AppConfig.Status.READ)
        }

    var status = AppConfig.Status.UPDATE
        set(value) {
            field = value
            binding.apply {
                val isUpdate = status == AppConfig.Status.UPDATE
                imgCamera.show(isUpdate)
            }
        }

    var backgroundID: Int = R.color.color_primary
        set(value) {
            binding.lvImage.setBackgroundResource(value)
            field = value
        }


    init {
        binding.apply {
            btClose.onClick {
                onClickClearImage.invoke(url.safe())
                removePhoto()
            }
        }

        context.loadAttrs(attrs, R.styleable.LoadingImageView) {
            status =
                AppConfig.Status.getDayByIndex(
                    it.getInt(
                        R.styleable.LoadingImageView_status,
                        AppConfig.Status.UPDATE.inx
                    )
                )
            enableRemoveImage =
                it.getBoolean(R.styleable.LoadingImageView_custom_enable_remove_image, true)
            backgroundID =
                it.getResourceId(
                    R.styleable.LoadingImageView_background,
                    R.drawable.bg_white_radius
                )
        }
    }

    var onClickClearImage: ((url: String) -> Unit) = {}

    private var url: String? = ""
        set(value) {
            field = value
            if (!url.isNullOrBlank()) {
                binding.apply {
                    imgCamera.hide()
                    lvImage.setBackgroundResource(R.color.transparency)
                    image.setImageURICustom(value)
                }
            }
        }

    fun removePhoto() {
        binding.apply {
            btClose.hide()
            lvAddImage.hide()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setImageUrl(url: String?) {
        binding.apply {
            this@LoadingImageView.url = url
        }
    }
}