package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutServicePriceViewBinding
import com.example.nailexpress.extension.toViewTypeService
import com.example.nailexpress.models.response.SkillDTO
import com.example.nailexpress.models.ui.main.Skill
import com.example.nailexpress.utils.Constant
import com.example.nailexpress.views.ui.main.customer.detailpost.adapter.NamePriceServiceAdapter
import com.example.nailexpress.views.ui.main.customer.detailpost.adapter.ViewTypeService

class ServicePriceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding = LayoutServicePriceViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private lateinit var namePriceServiceAdapter: NamePriceServiceAdapter

    private val gridLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(context, NUM_OF_ROW, GridLayoutManager.VERTICAL, false)
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    var spTitle: String? = null
        set(value) {
            value?.let {
                binding.tvTitle.text = it
            }
            field = value
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ServicePriceView, 0, 0).run {
            try {
                val title = getString(R.styleable.ServicePriceView_spv_title) ?: Constant.EMPTY
                with(binding) {
                    tvTitle.apply {
                        text = title
                    }
                }
            } finally {
                recycle()
            }
        }
    }

    fun setListService(list: List<SkillDTO>?) {
        if (list.isNullOrEmpty().not()) {
            list?.firstOrNull()?.price?.toViewTypeService()?.let { viewTypeService ->
                binding.tvTitle.apply {
                    text = when (viewTypeService) {
                        ViewTypeService.Name -> context.getString(R.string.service_can_do)
                        ViewTypeService.NameAndPrice -> context.getString(R.string.list_service)
                    }
                }
                namePriceServiceAdapter = NamePriceServiceAdapter(viewType = viewTypeService)
                binding.recyclerView.apply {
                    layoutManager = when (viewTypeService) {
                        ViewTypeService.Name -> gridLayoutManager
                        ViewTypeService.NameAndPrice -> linearLayoutManager
                    }
                    adapter = namePriceServiceAdapter
                }
                namePriceServiceAdapter.setData(list)
            }

        }
    }

    companion object {
        private const val NUM_OF_ROW = 2
    }
}