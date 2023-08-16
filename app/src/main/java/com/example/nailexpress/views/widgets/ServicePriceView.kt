package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.nailexpress.app.SalaryType
import com.example.nailexpress.databinding.LayoutServicePriceViewBinding
import com.example.nailexpress.extension.safe
import com.example.nailexpress.extension.show
import com.example.nailexpress.extension.toSalaryType
import com.example.nailexpress.models.response.SkillDTO
import com.example.nailexpress.views.ui.main.customer.detailpost.adapter.NamePriceServiceAdapter

class ServicePriceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val binding = LayoutServicePriceViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private lateinit var listServiceAdapter: NamePriceServiceAdapter
    private lateinit var listTimeAdapter: NamePriceServiceAdapter
    private var status: SalaryType = SalaryType.Time
        set(value) {
            field = value
            binding.apply {
                listOf(tvTitleService, recyclerService).show(SalaryType.Service == value || value == SalaryType.Both)
                listOf(tvTitleTime, recyclerTime).show(SalaryType.Time == value || value == SalaryType.Both)
            }
        }

    fun setListService(status: Int,list: List<SkillDTO>?) {
        this.status = SalaryType.getSalaryType(status)

        val listSkillService = list.safe().filter { it.price.toSalaryType() == SalaryType.Service }
        val listSkillTime = list.safe().filter { it.price.toSalaryType() == SalaryType.Time }

        if(listSkillService.isNotEmpty()){
            listServiceAdapter = NamePriceServiceAdapter(viewType = SalaryType.Service)
            binding.recyclerService.adapter = listServiceAdapter
            listServiceAdapter.setData(listSkillService)
        }
        if(listSkillTime.isNotEmpty()){
            listTimeAdapter = NamePriceServiceAdapter(viewType = SalaryType.Time)
            binding.recyclerTime.adapter = listTimeAdapter
            listTimeAdapter.setData(listSkillTime)
        }
    }

    fun setTimeTitle(content: String){
        binding.tvTitleTime.text = "Danh sách Theo thời gian ($content)"
    }

    companion object {
        private const val NUM_OF_ROW = 2
    }
}