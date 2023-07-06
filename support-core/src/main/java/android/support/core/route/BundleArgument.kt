package android.support.core.route

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment

interface BundleArgument : Parcelable {

    fun toBundle(): Bundle {
        return Bundle().also {
            it.putParcelable(KEY, this)
        }
    }

    @Suppress("unchecked_cast")
    companion object {
        const val KEY = "BundleArgument"

        operator fun <T : BundleArgument> get(argument: Intent?): T {
            return argument?.extras?.getParcelable(KEY) ?: error("Not found argument")
        }

        fun <T : BundleArgument> of(argument: Intent?): T? {
            return argument?.extras?.getParcelable(KEY) as? T
        }

        operator fun <T : BundleArgument> get(argument: Bundle?): T {
            return argument?.getParcelable(KEY) ?: error("Not found argument")
        }

        fun <T : BundleArgument> of(argument: Bundle?): T? {
            return argument?.getParcelable(KEY)
        }
    }
}

fun <T : BundleArgument> argumentOf(intent: Intent?, def: (() -> T)? = null): T {
    if (def == null) return BundleArgument[intent]
    return BundleArgument.of(intent) ?: def()
}

fun <T : BundleArgument> argumentOf(bundle: Bundle?, def: (() -> T)? = null): T {
    if (def == null) return BundleArgument[bundle]
    return BundleArgument.of(bundle) ?: def()
}

fun <T : BundleArgument> Activity.argument(def: (() -> T)? = null): T {
    if (def == null) {
        return BundleArgument[intent]
    }
    return BundleArgument.of(intent) ?: def()
}

fun <T : BundleArgument?> Activity.nullableArguments(def: (() -> T)? = null): T? {
    if (def == null) {
            return BundleArgument.of(intent)
    }
    return BundleArgument.of(intent) ?: def()
}

fun <T : BundleArgument> Fragment.argument(def: (() -> T)? = null): T {
    if (def == null) {
        return BundleArgument[arguments]
    }
    return BundleArgument.of(arguments) ?: def()
}

fun <T : BundleArgument?> Fragment.nullableArguments(def: (() -> T)? = null): T? {
    if (def == null) {
        return if (this.arguments != null)
            BundleArgument[arguments] else null
    }
    return BundleArgument.of(arguments) ?: def()
}


fun <T : BundleArgument> Activity.lazyArgument(def: (() -> T)? = null) =
    lazy(LazyThreadSafetyMode.NONE) {
        argument(def)
    }
fun <T : BundleArgument> Activity.lazyNullableArgument(def: (() -> T)? = null) =
    lazy(LazyThreadSafetyMode.NONE) {
        argument(def)
    }

fun <T : BundleArgument> Fragment.lazyArgument(def: (() -> T)? = null) =
    lazy(LazyThreadSafetyMode.NONE) {
        argument(def)
    }