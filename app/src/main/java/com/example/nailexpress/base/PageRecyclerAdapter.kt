package com.example.nailexpress.base

import android.support.core.view.ILoadMoreAction
import android.support.core.view.PageRecyclerAdapter
import android.support.core.view.BaseViewHolder
import android.support.core.view.dataBindingInflateOf
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.nailexpress.app.AppConfig

abstract class PageRecyclerAdapter<T, V : ViewDataBinding>(actionLoad: ILoadMoreAction) :
    PageRecyclerAdapter<T, V>(AppConfig.perPage, actionLoadMore = actionLoad) {

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<V> {
        return BaseViewHolder(
            parent.dataBindingInflateOf(layoutId)
        )
    }

    @get:LayoutRes
    protected abstract val layoutId: Int
}