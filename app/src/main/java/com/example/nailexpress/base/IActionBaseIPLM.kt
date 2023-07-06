package com.example.nailexpress.base

import android.content.Context
import android.support.core.view.ViewScopeOwner
import android.widget.Toast
import com.example.nailexpress.extension.asFragmentActivity
import com.example.nailexpress.helper.network.HandleException
import com.example.nailexpress.models.ui.base.DialogData

interface IActionBaseIPLM:ViewScopeOwner {
    val TAG get() = this.javaClass.simpleName

    val appActivity get() = this.asFragmentActivity()
    val mContext get() = appActivity.baseContext


//    val handleException: HandleException
//        get() = HandleException.getInstance()

    fun showAlertDialog(data : DialogData) {
        appActivity.commonDialog.show(data)
    }

    fun showToast(content: String, duration : Int = Toast.LENGTH_LONG) {
        Toast.makeText(appActivity,content,duration).show()
    }

    fun showToast(res: Int, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(appActivity,getStringRes(res),duration).show()
    }

    fun closeApp() {
        appActivity.finishAffinity()
    }

    fun onClearViewModelInScopeActivity() {
        appActivity.viewModelStore.clear()
    }

    fun getString(res: Int, params: Any) = mContext.getString(res, params)

    fun getStringRes(res: Int) = appActivity.getString(res)
}