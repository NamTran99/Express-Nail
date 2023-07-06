package com.example.nailexpress.app

import android.content.Intent
import android.support.core.view.ViewScopeOwner

interface ResultLifecycleInf {
    fun onActivityResult(callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit)
}

class ResultsLifecycle(): ResultLifecycleInf {
    private val mActivityResults = hashSetOf<(Int, Int, Intent?) -> Unit>()

    override fun onActivityResult(callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit) {
        mActivityResults.add(callback)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mActivityResults.forEach { it(requestCode, resultCode, data) }
        mActivityResults.clear()
    }
}

interface ResultsLifecycleOwner : ViewScopeOwner {
    val resultLifecycle
        get() = viewScope.getOr("app_settings") {
            ResultsLifecycle()
        }
}