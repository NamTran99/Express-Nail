package com.example.nailexpress.views.widgets.staff

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nailexpress.R
import com.example.nailexpress.databinding.LayoutServicesWorkerBinding
import com.example.nailexpress.models.response.SkillDTO
import com.example.nailexpress.views.ui.main.staff.adapter.ServicesWorkerAdapter
import com.example.nailexpress.views.widgets.divider.VerticalDivider

class ServicesWorkerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding = LayoutServicesWorkerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val servicesWorkerAdapter: ServicesWorkerAdapter by lazy {
        ServicesWorkerAdapter()
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ServicesWorkerView, 0, 0).run {
            try {
                val title = getString(R.styleable.ServicesWorkerView_sw_title)
                val itemSpace = getFloat(
                    R.styleable.ServicesWorkerView_sw_itemSpace,
                    ITEM_SPACE_DEFAULT.toFloat()
                )
                val titleColor = getColor(
                    R.styleable.ServicesWorkerView_sw_colorTitle,
                    ContextCompat.getColor(context, R.color.colorPrimary)
                )
                setTitle(title)
                setTitleColor(titleColor)
                setUpRecyclerView(itemSpace)
            } finally {
                recycle()
            }
        }
    }

    fun setTitle(title: String?) {
        title?.let {
            binding.tvTitle.text = it
        }
    }

    fun setTitleColor(@ColorInt color: Int) {
        binding.tvTitle.setTextColor(color)
    }

    private fun setUpRecyclerView(itemSpace: Number = ITEM_SPACE_DEFAULT) {
        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = servicesWorkerAdapter
            addItemDecoration(VerticalDivider(itemSpace))
        }
    }

    fun setDataListService(list: List<SkillDTO>?) {
        servicesWorkerAdapter.setData(list)
    }

    companion object {
        private const val ITEM_SPACE_DEFAULT = 12
    }
}