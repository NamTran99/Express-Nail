package android.support.core.view

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerAdapter<T : Any, VB : ViewDataBinding> :
    RecyclerView.Adapter<BaseViewHolder<VB>>() {

    val TAG: String = this::class.java.simpleName

    private var mRecyclerView: RecyclerView? = null

    protected var asynList = AsyncListDiffer(this, DiffCallback<T>())
    val mitems get() = asynList.currentList.toMutableList()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.itemAnimator = null
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
    }

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("unchecked_cast")
    open fun submit(newItems: List<T>) {
        asynList.submitList(null)
        asynList.submitList(
            newItems
        )
    }

    fun getItem(pos: Int): T? = asynList.currentList.getOrNull(pos)

    open fun addData(item: T) {
        val oldList = mitems
        oldList.add(item)
        asynList.submitList(oldList)
    }

    open fun removeData(item: T) {
        val oldList = mitems
        oldList.remove(item)
        asynList.submitList(oldList)
    }

    open fun clear() {
        asynList.submitList(listOf())
    }

    open fun refreshData() {
        asynList.submitList(mitems)
    }

    private fun findLastVisibleItem(): Int {
        return when (val layoutManager = mRecyclerView!!.layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> error("Not support ${layoutManager?.javaClass?.name}")
        }
    }

    fun findFirstVisibleItem(): Int {
        return when (val layoutManager = mRecyclerView!!.layoutManager) {
            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> error("Not support ${layoutManager?.javaClass?.name}")
        }
    }

    override fun getItemCount(): Int = asynList.currentList.count()
}

open class BaseViewHolder<VB : ViewDataBinding>(open val binding: VB) : RecyclerView.ViewHolder(binding.root)

interface ViewHolderExtension<T>{
    fun bind(item: T)
}