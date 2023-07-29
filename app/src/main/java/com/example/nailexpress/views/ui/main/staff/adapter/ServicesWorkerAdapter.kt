package com.example.nailexpress.views.ui.main.staff.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nailexpress.base.BaseAdapter
import com.example.nailexpress.base.BaseVH
import com.example.nailexpress.databinding.ItemServiceWorkerBinding
import com.example.nailexpress.models.ui.main.Skill

class ServicesWorkerAdapter : BaseAdapter<Skill, ServicesWorkerAdapter.ServiceWorkerVH>() {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseVH<Skill> {
        val binding = ItemServiceWorkerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ServiceWorkerVH(binding)
    }

    inner class ServiceWorkerVH(private val binding: ItemServiceWorkerBinding) : BaseVH<Skill>(binding.root) {
        override fun bindView(data: Skill) {
            with(binding) {
                tvName.text = data.name
                tvPrice.text = data.price_display
            }
        }
    }
}