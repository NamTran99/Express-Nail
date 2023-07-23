package com.example.nailexpress.exception

import android.support.core.extensions.block
import android.view.View
import android.widget.EditText
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseActivity
import com.example.nailexpress.extension.safe
import com.example.nailexpress.extension.showKeyboard
import com.example.nailexpress.models.ui.base.DialogData
import java.net.UnknownHostException

interface ErrorHandler {
    fun handle(activity: BaseActivity<*>, error: Throwable)
//    fun handle(fragment: BaseFragment<*, *>, error: Throwable)
}


class ErrorHandlerImpl : ErrorHandler {

    override fun handle(activity: BaseActivity<*>, error: Throwable) = block(activity) {
        when (error) {
            is ViewErrorCustom -> {
                val view =
                    activity.findViewById<View>(error.viewId)
                if (view is IViewCustomerErrorHandler) {
                    view.handleError(error.res)
                }
                return
            }
            is ViewError -> {
                val view =
                    activity.findViewById<EditText>(error.viewId)
                view.run {
                    this.error = activity.getString(error.res)
                    showKeyboard()
                }
                return
            }
            is ResourceException -> {
                activity.toast(error.resource)
            }
            is UnauthorizedException -> {
                activity.commonDialog.show(DialogData().buildError(error.message.toString()))
            }
            is UnknownHostException -> {
                activity.commonDialog.show(
                    DialogData().buildError(
                        R.string.error_not_connect_network,
                        R.string.retry
                    ).setCallBack {
                        activity.reloadData.invoke()
//                        activity.recreate()
                    }
                )
            }
            else -> activity.commonDialog.show(DialogData().buildError(error.message.safe()))
        }
    }

//    override fun handle(fragment: BaseFragment<*, *>, error: Throwable) = block(fragment) {
//        val activity = fragment.activity.cast<BaseActivity<*>>()
//        activity?.let { handle(it, fragment as RouteDispatcher, error) }
//            ?: error("Fragment should be part of AppActivity")
//    }

//    private fun handle(activity: BaseActivity<*>, error: Throwable) {
//
//    }

}

