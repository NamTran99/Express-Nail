package com.example.nailexpress.extension

import android.support.core.event.ErrorEvent
import android.support.core.event.LoadingEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.utils.ViewModelHandleUtils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


fun BaseViewModel.launch(
    loading: LoadingEvent? = ViewModelHandleUtils.isLoading,
    error: ErrorEvent? = ViewModelHandleUtils.isError,
    context: CoroutineContext = EmptyCoroutineContext,
    isBlockOther: Boolean = false,
    function: suspend CoroutineScope.() -> Unit
) {
    val handler = CoroutineExceptionHandler { _, throwable ->
        if (throwable !is CancellationException) {
            throwable.printStackTrace()
            error?.post(throwable)
            loading?.post(false)
        }
    }
    if (isBlockOther) context.cancelChildren()
    viewModelScope.launch(context = context + handler) {
        loading?.post(true)
        function()
        loading?.post(false)
    }
}