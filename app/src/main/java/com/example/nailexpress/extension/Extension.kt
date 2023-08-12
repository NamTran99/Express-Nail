package com.example.nailexpress.extension;

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.nailexpress.app.AppConfig.REQUEST_CODE_SHARE_APP
import com.example.nailexpress.base.BaseActivity
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.views.ui.main.customer.detailpost.adapter.ViewTypeService
import com.google.gson.Gson
import java.io.File
import kotlin.math.roundToInt


fun AppCompatActivity.hideStatusBarAndNavigationBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.navigationBarColor = Color.TRANSPARENT
    }
}

fun AppCompatActivity.setHideStatusBarAndControlBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}

fun Context.getNavigationBarHeight(): Int {
    val resources: Resources = this.resources
    val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0
}

fun Context.getStatusBarHeight(): Int {
    val resources: Resources = this.resources
    val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0
}

fun Any?.toJson() = Gson().toJson(this)

inline fun <reified T> String.fromJson() = Gson().fromJson(this, T::class.java)

/**
 * "app at: https://play.google.com/store/apps/details?id=$applicationId"
 */
fun Activity.shareApp(content: String, title: String) {
    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.type = "text/plain"
    shareIntent.putExtra(
        Intent.EXTRA_TEXT, content
    )
    startActivityForResult(
        Intent.createChooser(
            shareIntent, title
        ), REQUEST_CODE_SHARE_APP
    )
}

/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun convertDpToPixel(dp: Float, context: Context): Float {
    return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

/**
 * This method converts device specific pixels to density independent pixels.
 *
 * @param px A value in px (pixels) unit. Which we need to convert into db
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent dp equivalent to px value
 */
fun convertPixelsToDp(px: Float, context: Context): Float {
    return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}


/**get cache from storage*/
fun Context.calculateSizeRecursively(): Long {
    return cacheDir.walkBottomUp().fold(0L) { acc, file -> acc + file.length() } / 1024
}

/**clear cache*/
fun Context.deleteCache() {
    try {
        val dir: File = cacheDir
        deleteDir(dir)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

//clear cache
fun deleteDir(dir: File?): Boolean {
    return if (dir != null && dir.isDirectory()) {
        val children: Array<String> = dir.list()
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
        dir.delete()
    } else if (dir != null && dir.isFile()) {
        dir.delete()
    } else {
        false
    }
}

fun Any.asFragmentActivity() =
    ((this as? BaseFragment<*, *>)?.requireActivity() ?: this) as BaseActivity<*>


typealias Callback = (() -> Unit)?

val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()

fun Double?.toViewTypeService(): ViewTypeService {
    return when (this) {
        ViewTypeService.Name.type.toDouble() -> ViewTypeService.Name
        ViewTypeService.NameAndPrice.type.toDouble() -> ViewTypeService.NameAndPrice
        else -> ViewTypeService.Name
    }
}

fun Double?.addPrefixDollar(): String {
    return if (this != null) {
        return "$$this"
    } else ""
}


fun NavController.clearBackStackAndNavigate(destinationId: Int) {
    visibleEntries.value.size.let {
        if (it > 0) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(
                    visibleEntries.value[0].destination.parent!!.id, true
                )
                .build()
            navigate(destinationId, null, navOptions)
        }
    }
}

fun Context.openMap(address: String) {
    val uri = "http://maps.google.co.in/maps?q=$address"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    intent.setPackage("com.google.android.apps.maps")

    startActivity(intent)
}
