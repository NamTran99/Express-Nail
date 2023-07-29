package com.example.nailexpress.base

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nailexpress.models.ui.main.Skill

typealias OnClickItemBaseAdapter<T> = (item: T, position: Int) -> Unit

abstract class BaseAdapter<T, VH : BaseVH<T>>(
    private val onClickItemBaseAdapter: OnClickItemBaseAdapter<T> = { _, _ -> }
) : RecyclerView.Adapter<BaseVH<T>>() {
    private var listData: MutableList<T> = mutableListOf()

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): BaseVH<T>

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<T>?) {
        listData = list as MutableList<T>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<T> {
        return getViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: BaseVH<T>, position: Int) {
        val itemData = listData[position]
        holder.bindView(itemData)
        holder.itemView.setOnClickListener {
            onClickItemBaseAdapter.invoke(itemData, position)
        }
    }
}

abstract class BaseVH<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindView(data: T)
}