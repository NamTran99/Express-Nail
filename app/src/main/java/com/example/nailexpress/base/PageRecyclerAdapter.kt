package com.example.nailexpress.base

import android.support.core.view.ILoadMoreAction
import android.support.core.view.PageRecyclerAdapter
import android.support.core.view.RecyclerHolder
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.nailexpress.app.AppConfig

abstract class PageRecyclerAdapter<T, V : ViewBinding>(actionLoad: ILoadMoreAction) :
    PageRecyclerAdapter<T>(AppConfig.perPage, actionLoadMore = actionLoad) {

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding: V = onCreateBinding(parent)
        return object : RecyclerHolder<T>(binding) {
            override fun bind(item: T) {
                super.bind(item)
                onBindHolder(item, binding, adapterPosition)
            }
        }
    }

    protected abstract fun onCreateBinding(parent: ViewGroup): V

    protected abstract fun onBindHolder(item: T, binding: V, adapterPosition: Int)
}