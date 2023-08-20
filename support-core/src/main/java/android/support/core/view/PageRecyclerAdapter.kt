package android.support.core.view
import android.annotation.SuppressLint
import android.support.core.funtional.OnLoadMoreListener
import android.util.Log
import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

interface ILoadMoreAction{
    val onLoadMoreListener: ((nextPage: Int,pageSize: Int) -> Unit)
}

@Suppress("UNCHECKED_CAST")
abstract class PageRecyclerAdapter<T, VB: ViewDataBinding>(
    private val pageSize: Int,
    private val threshold: Int = 0,
    private val mRevert: Boolean = false,
    val actionLoadMore: ILoadMoreAction
) : RecyclerView.Adapter<BaseViewHolder<VB>>() {
    companion object {
        private const val STATE_INITIAL = -1
        private const val STATE_LOADING = 1
        private const val STATE_UNLOAD = 0
    }

    private var mState = STATE_INITIAL
    private val mItems = PageList<T>(pageSize)
    var onLoadMoreListener: ((Int, Int) -> Unit)= actionLoadMore.onLoadMoreListener

    private var mLoadMoreListener = object : OnLoadMoreListener(threshold, mRevert) {
        override fun onLoadMore() {
            if (!mItems.shouldLoadMore) return
            if (mState == STATE_LOADING) return
            mState = STATE_LOADING
            val nextPage = mItems.nextPage
            this@PageRecyclerAdapter.onLoadMore(nextPage, pageSize)
            onLoadMoreListener?.invoke(nextPage, pageSize)
        }
    }

    init {
        mItems.onItemsInserted = { from, count ->
            if (count == 1) notifyItemInserted(from)
            else notifyItemRangeInserted(from, count)
        }
    }

    var isLoading: Boolean
        get() = mState == STATE_LOADING
        set(value) {
            mState = if (value) STATE_LOADING else STATE_UNLOAD
        }

    @MainThread
    open fun addAll(items: List<T>?) {
//        clear()
        if (items != null) mItems.addAll(items)
    }

    @MainThread
    open fun submitAll(items: List<T>?) {
        clear()
        if (items != null) mItems.addAll(items)
    }

    @MainThread
    open fun addAll(items: List<T>?, pageNumber: Int = 1) {
        Log.d("TAG", "submit: $pageNumber")
        if(pageNumber ==1) clear()
        if (items != null) mItems.addAll(items)
    }

    override fun getItemCount() = mItems.size
    fun items() = mItems

    fun getData(): MutableList<T> {
        return mItems.getData()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(mLoadMoreListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView.removeOnScrollListener(mLoadMoreListener)
    }

    protected open fun onLoadMore(nextPage: Int, pageSize: Int) {

    }

    @Suppress("unchecked_cast")
    override fun onBindViewHolder(holder: BaseViewHolder<VB>, p1: Int) {
        onBindHolder(mItems[p1], holder.binding, p1)
    }

    protected abstract fun onBindHolder(item: T, binding: VB, adapterPosition: Int)

//    override fun onBindViewHolder(
//        holder: BaseViewHolder<VB>,
//        position: Int,
//        payloads: MutableList<Any>
//    ) {
//        super.onBindViewHolder(holder, position, payloads)
////        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
////        else (holder as? BaseViewHolder<T>)?.bind(getData()[position], payloads)
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        mItems.clear()
        notifyDataSetChanged()
    }
}

class PageList<T>(
    private val pageSize: Int
) {
    internal lateinit var onItemsInserted: (from: Int, count: Int) -> Unit
    val nextPage get() = (mItems.size / pageSize) + 1

    var shouldLoadMore: Boolean = true
        private set

    private val mItems = arrayListOf<T>()
    val size get() = mItems.size

    operator fun get(index: Int) = mItems[index]

    fun addAll(items: List<T>) {
        val oldSize = mItems.size
        mItems.addAll(items)
        if (items.size != pageSize) shouldLoadMore = false
        onItemsInserted(oldSize, items.size)
    }

    fun getData(): MutableList<T> {
        return mItems
    }

    fun clear() {
        mItems.clear()
        shouldLoadMore = true
    }

}