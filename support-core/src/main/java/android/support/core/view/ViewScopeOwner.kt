package android.support.core.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.support.core.R
import android.support.core.route.RouteDispatcher
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

interface ViewScopeOwner {
    val viewScope: ViewScope
        get() {
            val view = when (this) {
                is Activity -> {
                    this.findViewById(android.R.id.content)
                }
                is ViewGroup -> this
                is Fragment -> requireView() as ViewGroup
                is Dialog -> this.findViewById(android.R.id.content)
                else -> error("${this.javaClass.name} is not ContextOwner")
            }
            var tag = view.getTag(R.id.view_scope) as? ViewScope
            if (tag == null) tag = ViewScope(view).also { view.setTag(R.id.view_scope, it) }
            return tag
        }
    val owner: LifecycleOwner?
        get() {
            return when (this) {
                is Activity -> this.application as LifecycleOwner
                is Fragment -> viewLifecycleOwner
                else -> null
            }
        }
    val routerDispatcher: RouteDispatcher?
        get() {
            return if (this is RouteDispatcher) this else null
        }
}

class ViewScope(private val view: ViewGroup) {
    val context: Context get() = view.context

    private val mCache = hashMapOf<String, Any>()

    init {
        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {}

            override fun onViewDetachedFromWindow(p0: View) {
                mCache.forEach { (_, v) ->
                    if (v is AutoCloseable) {
                        v.close()
                    }
                }
                mCache.clear()
                view.removeOnAttachStateChangeListener(this)
            }
        })
    }

    operator fun set(key: String, value: Any) {
        mCache[key] = value
    }

    @Suppress("unchecked_cast")
    operator fun <T> get(key: String): T? {
        return mCache[key] as? T
    }

    @Suppress("unchecked_cast")
    fun <T> getOr(key: String, factory: () -> T): T {
        return mCache[key] as? T ?: factory().also { mCache[key] = it!! }
    }
}

