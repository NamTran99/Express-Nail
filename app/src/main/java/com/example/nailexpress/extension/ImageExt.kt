package com.example.nailexpress.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.nailexpress.R
import com.example.nailexpress.base.MainApplication
import com.example.nailexpress.models.ui.AppImage
import com.squareup.picasso.Transformation
import okhttp3.MultipartBody


/**
 *  Dùng cho picasso
 *  case load ảnh bằng picasso những ảnh portrait bị rotate 90 dộ
 *
 */
class RotateTransformation(private val rotateRotationAngle: Int) : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(rotateRotationAngle.toFloat())
        val bitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        if (bitmap != source) {
            source.recycle()
        }
        return bitmap
    }

    override fun key(): String {
        return "rotate$rotateRotationAngle"
    }
}

fun getRotateDegree(context: Context, imageURI: String): Int {
    val input = context.contentResolver.openInputStream(imageURI.toUri())
    val ei: ExifInterface =
        if (Build.VERSION.SDK_INT > 23) ExifInterface(input!!) else ExifInterface(
            imageURI
        )
    val orientation =
        ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    var rotationDegrees = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }
    return rotationDegrees
}

// use for result of selected image
fun ArrayList<Uri>.convertToResult(): List<AppImage> {
    return this.map { pathUri -> AppImage(image = pathUri.toString()) }
}

fun List<AppImage>.convertToRequest(): ArrayList<Uri>{
  return ArrayList(this.map { it.image.toUri() })
}

fun List<AppImage>.filterRemoteImage(): List<AppImage> = filter { !it.image.contains("http") }
suspend fun List<AppImage>.toArrayPart(key: String): Array<MultipartBody.Part?>  {
    val context = MainApplication.application
    return mapIndexed { index, uriLink ->
        context.getFilePath(uriLink.image.toUri())!!.scalePhotoLibrary(context)
            .toImagePart(key)
    }.toTypedArray()
}

suspend fun String.toScaleImagePart(key: String): MultipartBody.Part? {
    if (isNullOrEmpty() || contains("http")) return null
    val context = MainApplication.application
    return  context.getFilePath(toUri())!!.scalePhotoLibrary(context)
        .toImagePart(key)
}

@BindingAdapter(value = ["src", "color", "size"], requireAll = false)
fun srcServer(
    imageView: ImageView,
    src: String?,
    color: Int?,
    size: Float?
) {
    if(src == null) return
    val circularProgressDrawable = CircularProgressDrawable(imageView.context).apply {
        strokeWidth = 5f
        centerRadius = size ?: 25F
        setColorSchemeColors(ContextCompat.getColor(imageView.context,color ?: R.color.white))
        start()
    }

    Glide.with(imageView.context)
        .load(src) //replace with contact image uri
        .placeholder(circularProgressDrawable)
        .error(R.drawable.img_error)
        .into(imageView)
}