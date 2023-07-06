package android.support.core.funtional

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class OnLoadMoreListener(
    private val mThreshold: Int,
    private val mRevert: Boolean = false
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = (recyclerView.layoutManager as LinearLayoutManager?)!!
        val lastPosition = layoutManager.itemCount - 1
        val orientation = layoutManager.orientation
        requestLoadMore(
            layoutManager,
            lastPosition,
            if (orientation == LinearLayoutManager.VERTICAL) dy else dx
        )
    }

    private fun requestLoadMore(
        layoutManager: LinearLayoutManager,
        lastPosition: Int,
        translate: Int
    ) {
        if (layoutManager.findLastVisibleItemPosition() + mThreshold >= lastPosition && if (mRevert) translate <= 0 else translate > 0)
            onLoadMore()
    }

    protected abstract fun onLoadMore()
}