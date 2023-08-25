package com.example.nailexpress.base

import android.support.core.view.BaseViewHolder
import android.support.core.view.RecyclerAdapter
import android.support.core.view.dataBindingInflateOf
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.example.nailexpress.views.ui.main.profile.adapters.ISelector

abstract class SimpleRecyclerAdapter<T : Any, VB : ViewDataBinding>() :
    RecyclerAdapter<T, VB>() {

    @get:LayoutRes
    protected abstract val layoutId: Int

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<VB> {
        return BaseViewHolder(
            parent.dataBindingInflateOf(layoutId)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        val cur = getItem(position)!!
        onBindHolder(cur, holder.binding, position)
    }

    protected abstract fun onBindHolder(item: T, binding: VB, adapterPosition: Int)
}

abstract class SimpleSelectorRecyclerAdapter<T : Any, VB : ViewDataBinding> :
    SimpleRecyclerAdapter<T, VB>() {

    fun selectOne(item: T) {
        mitems.forEachIndexed { index, roleOption ->
            roleOption as ISelector<*>
            if (roleOption == item) {
                roleOption.isCheck = true
                notifyItemChanged(index)
            } else {
                if (roleOption.isCheck) {
                    roleOption.isCheck = false
                    notifyItemChanged(index)
                }
            }
        }
    }

    fun selectMore(item: T){
        mitems.forEachIndexed { index, roleOption ->
            roleOption as ISelector<*>
            if (roleOption == item) {
                roleOption.isCheck = !roleOption.isCheck
                notifyItemChanged(index)
            }
        }
    }
}

//abstract class SimpleViewPagerAdapter<T: Any, V : ViewBinding>(protected val view: ViewPager2) :
//    RecyclerAdapter<T>() {
//    init {
//        view.adapter = this
//    }
//
//    final override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): RecyclerView.ViewHolder {
//        val binding: V = onCreateBinding(parent)
//        return object : RecyclerHolder<T>(binding) {
//            override fun bind(item: T) {
//                super.bind(item)
//                onBindHolder(item, binding, adapterPosition)
//            }
//        }
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    override fun clear() {
//        items?.clear()
//        notifyDataSetChanged()
//    }
//
//    protected abstract fun onCreateBinding(parent: ViewGroup): V
//
//    protected abstract fun onBindHolder(item: T, binding: V, adapterPosition: Int)
//}