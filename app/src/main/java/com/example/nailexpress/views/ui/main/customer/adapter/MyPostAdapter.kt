package com.example.nailexpress.views.ui.main.customer.adapter

import android.support.core.view.ILoadMoreAction
import com.example.nailexpress.R
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.databinding.ItRvMyPostBinding
import com.example.nailexpress.databinding.ItemListStaffBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.setImageURICustom
import com.example.nailexpress.models.ui.main.Cv
import com.example.nailexpress.models.ui.main.RecruitmentForm

interface IMyPostAction: ILoadMoreAction {
    val onClickDetail: ((cvID: Int) -> Unit)
}

class MyPostAdapter(val action: IMyPostAction): PageRecyclerAdapter<RecruitmentForm, ItRvMyPostBinding>(action) {

    override fun onBindHolder(item: RecruitmentForm, binding: ItRvMyPostBinding, adapterPosition: Int) {
        binding.apply {
            data = item
            item.image_url?.let {
                ivLogo.setImageURICustom(it)
            }

            btnDetail.setOnClickListener {
                item.id?.let { it1 -> action.onClickDetail(it1) }
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.it_rv_my_post
}