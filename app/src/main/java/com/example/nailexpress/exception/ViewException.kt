package com.example.nailexpress.exception

import androidx.annotation.IdRes
import androidx.annotation.StringRes

class ViewError(val viewId: Int?, val res: Int) : Throwable()

fun showError(@IdRes viewId: Int? = null, @StringRes errorID: Int): Nothing = throw ViewError(viewId, errorID)

//class ViewErrorCustom(val viewId: Int, val res: Int) : Throwable()
//
//fun viewErrorCustom(@IdRes viewId: Int, @StringRes res: Int): Nothing =
//    throw ViewErrorCustom(viewId, res)

//class ViewPassInputError(val viewId: Int) : Throwable()

//fun viewPassInputError(@IdRes viewId: Int): Nothing = throw ViewPassInputError(viewId)

//class ResourceException(val resource: Int) : Throwable()
//
//fun resourceError(@StringRes res: Int): Nothing = throw ResourceException(res)
//
//fun showErrorCustom(@StringRes error: Int, @IdRes viewId: Int? = null){
//    viewError(viewId, error)
//}
