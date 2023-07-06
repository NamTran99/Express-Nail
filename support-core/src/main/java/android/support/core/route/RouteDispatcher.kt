package android.support.core.route

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

interface RouteDispatcher

inline fun <reified T : FragmentActivity> RouteDispatcher.open(
    args: Bundle? = null,
    edit: Intent.() -> Unit = {}
): RouteDispatcher {
    when (this) {
        is AppCompatActivity -> startActivity(Intent(this, T::class.java).apply {
            if (args != null) putExtras(args)
            edit()
        })
        is Fragment -> startActivity(Intent(requireContext(), T::class.java).apply {
            if (args != null) putExtras(args)
            edit()
        })
    }
    return this
}

inline fun <reified T : Activity> RouteDispatcher.open1(
    args: Bundle? = null,
    edit: Intent.() -> Unit = {}
): RouteDispatcher {
    when (this) {
        is AppCompatActivity -> startActivity(Intent(this, T::class.java).apply {
            if (args != null) putExtras(args)
            edit()
        })
        is Fragment -> startActivity(Intent(requireContext(), T::class.java).apply {
            if (args != null) putExtras(args)
            edit()
        })
    }
    return this
}

inline fun <reified T : FragmentActivity> RouteDispatcher.open(args: BundleArgument): RouteDispatcher {
    return open<T>(args.toBundle())
}

inline fun <reified T : Activity> RouteDispatcher.open1(args: BundleArgument): RouteDispatcher {
    return open1<T>(args.toBundle())
}

inline fun <reified T : FragmentActivity> RouteDispatcher.withOpen(
    noinline callback: (ActivityResult) -> Unit
): OpenForResultAction {
    return OpenForResultActionImpl(this as ActivityResultRegister, T::class, callback)
}

inline fun <reified T : FragmentActivity> RouteDispatcher.withOpenSuccess(
    noinline callback: (Intent?) -> Unit
): OpenForResultAction {
    return OpenForResultOkAction(this as ActivityResultRegister, T::class, callback)
}

fun Intent.putArgs(args: Bundle?): Intent {
    args ?: return this
    putExtras(args)
    return this
}

fun Intent.putArgs(args: BundleArgument?): Intent {
    args ?: return this
    putExtras(args.toBundle())
    return this
}

fun RouteDispatcher.close() {
    when (this) {
        is AppCompatActivity -> finish()
        is Fragment -> requireActivity().finish()
    }
}

fun RouteDispatcher.close(code: Int, data: BundleArgument) {
    close(code, data.toBundle())
}

fun RouteDispatcher.close(code: Int, data: Bundle? = null) {
    fun FragmentActivity.doClose() {
        if (data != null) setResult(code, Intent().apply { putExtras(data) })
        else setResult(code)
        finish()
    }
    when (this) {
        is AppCompatActivity -> doClose()
        is Fragment -> requireActivity().doClose()
    }
}

fun RouteDispatcher.clear(): RouteDispatcher {
    when (this) {
        is AppCompatActivity -> finishAffinity()
        is Fragment -> requireActivity().finishAffinity()
    }
    return this
}