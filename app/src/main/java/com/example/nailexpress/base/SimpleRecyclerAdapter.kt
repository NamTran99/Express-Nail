package com.example.nailexpress.base

import android.annotation.SuppressLint
import android.support.core.view.RecyclerAdapter
import android.support.core.view.RecyclerHolder
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2

abstract class SimpleRecyclerAdapter<T, V : ViewBinding>() :
    RecyclerAdapter<T>() {

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

    @SuppressLint("NotifyDataSetChanged")
    override fun clear() {
        items?.clear()
        notifyDataSetChanged()
    }

    protected abstract fun onCreateBinding(parent: ViewGroup): V

    protected abstract fun onBindHolder(item: T, binding: V, adapterPosition: Int)
}

abstract class SimpleViewPagerAdapter<T, V : ViewBinding>(protected val view: ViewPager2) :
    RecyclerAdapter<T>() {
    init {
        view.adapter = this
    }

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

    @SuppressLint("NotifyDataSetChanged")
    override fun clear() {
        items?.clear()
        notifyDataSetChanged()
    }

    protected abstract fun onCreateBinding(parent: ViewGroup): V

    protected abstract fun onBindHolder(item: T, binding: V, adapterPosition: Int)
}