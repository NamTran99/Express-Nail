package android.support.core.view

import android.app.Activity
import android.content.Intent

val Activity.shouldPreventOpenFromIconApp: Boolean
    get() = !isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && Intent.ACTION_MAIN == intent.action

