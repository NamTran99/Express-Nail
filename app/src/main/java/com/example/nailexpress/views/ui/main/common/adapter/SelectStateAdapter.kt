package com.example.nailexpress.views.ui.main.common.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.nailexpress.R
import com.example.nailexpress.base.SimpleRecyclerAdapter
import com.example.nailexpress.databinding.ItemStateFindingBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.models.ui.main.StateDTO


class SelectStateAdapter(view: RecyclerView) :
    SimpleRecyclerAdapter<StateDTO, ItemStateFindingBinding>() {
    init {
        view.adapter = this
    }

    var onClickItemState: ((StateDTO) -> Unit) = {}

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindHolder(
        item: StateDTO,
        binding: ItemStateFindingBinding,
        adapterPosition: Int
    ) {
        binding.apply {
            tvState.text = item.state
            tvAcronym.text = item.state_code
            root.onClick {
                onClickItemState.invoke(item)
            }
        }
    }

    override val layoutId = R.layout.item_state_finding
}