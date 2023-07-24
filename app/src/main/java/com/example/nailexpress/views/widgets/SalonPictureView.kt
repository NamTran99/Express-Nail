package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutSalonPictureViewBinding
import com.example.nailexpress.models.ui.AppImage
import com.example.nailexpress.views.ui.main.customer.detailpost.adapter.SalonPictureAdapter

class SalonPictureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding = LayoutSalonPictureViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val salonPictureAdapter: SalonPictureAdapter by lazy {
        SalonPictureAdapter()
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.SalonPictureView, 0, 0).run {
            try {
                val title = getString(R.styleable.SalonPictureView_sp_title)
                with(binding) {
                    title?.let {
                        tvTitle.text = it
                    }
                    setUpRecyclerView()
                }
            } finally {
                recycle()
            }
        }
    }

    fun setListPicture(list: List<AppImage>?) {
        salonPictureAdapter.setData(list)
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = salonPictureAdapter
        }
    }
}