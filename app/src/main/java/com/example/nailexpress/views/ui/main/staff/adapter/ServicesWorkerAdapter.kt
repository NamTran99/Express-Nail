package com.example.nailexpress.views.ui.main.staff.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.nailexpress.base.BaseAdapter
import com.example.nailexpress.base.BaseVH
import com.example.nailexpress.databinding.ItemServiceWorkerBinding
import com.example.nailexpress.extension.addPrefixDollar
import com.example.nailexpress.factory.TextFormatter
import com.example.nailexpress.models.response.SkillDTO
import com.example.nailexpress.models.ui.main.Skill

class ServicesWorkerAdapter : BaseAdapter<SkillDTO, ServicesWorkerAdapter.ServiceWorkerVH>() {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseVH<SkillDTO> {
        val binding = ItemServiceWorkerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ServiceWorkerVH(binding)
    }

    inner class ServiceWorkerVH(private val binding: ItemServiceWorkerBinding) : BaseVH<SkillDTO>(binding.root) {
        override fun bindView(data: SkillDTO) {
            with(binding) {
                tvName.text = if (data.name.isNullOrBlank()) data.custom_skill else data.name
                tvPrice.apply {
                    isVisible = (data.price ?: 0.0) > 0.0
                    text = data.price.addPrefixDollar()
                }
            }
        }
    }
}