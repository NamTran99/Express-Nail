package com.example.nailexpress.base

import android.os.Bundle
import android.support.core.event.WindowStatusOwner
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nailexpress.R
import com.example.nailexpress.extension.colorSchemeDefault

abstract class BaseRefreshFragment<T : ViewDataBinding, VM : BaseViewModel>(layoutId: Int) : BaseFragment<T,VM>(layoutId) {
    protected abstract fun onRefreshListener()
    private var mLoadingRefreshView: SwipeRefreshLayout? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingRefreshView = view.findViewById(R.id.viewRefresh)
        mLoadingRefreshView?.colorSchemeDefault()
        mLoadingRefreshView?.setOnRefreshListener { onRefreshListener() }
        registerRefreshLoading()
    }

    open fun registerRefreshLoading() {}

    fun showLoadingRefresh(it: Boolean?) {
        mLoadingRefreshView?.isRefreshing = it!!
    }
}