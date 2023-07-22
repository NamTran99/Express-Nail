package com.example.nailexpress.base

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nailexpress.R
import com.example.nailexpress.extension.colorSchemeDefault

abstract class BaseRefreshFragment<T : ViewDataBinding, VM : BaseRefreshViewModel>(layoutId: Int) : BaseFragment<T,VM>(layoutId) {
    private var mLoadingRefreshView: SwipeRefreshLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingRefreshView = view.findViewById(R.id.viewRefresh)
        mLoadingRefreshView?.colorSchemeDefault()
        mLoadingRefreshView?.setOnRefreshListener { viewModel.onSwipeRefreshData()}
        registerRefreshLoading()
    }

    open fun registerRefreshLoading() {
        viewModel.refreshLoading.observe(viewLifecycleOwner){
            showLoadingRefresh(it)
        }
    }

    private fun showLoadingRefresh(it: Boolean?) {
        mLoadingRefreshView?.isRefreshing = it!!
    }
}