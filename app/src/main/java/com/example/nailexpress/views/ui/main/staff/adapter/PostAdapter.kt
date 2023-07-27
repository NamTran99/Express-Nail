package com.example.nailexpress.views.ui.main.staff.adapter

import android.support.core.view.ILoadMoreAction
import com.example.nailexpress.R
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.databinding.ItRvPostStaffBinding
import com.example.nailexpress.extension.setImageURICustom
import com.example.nailexpress.models.ui.main.RecruitmentForm

interface IPostAction : ILoadMoreAction {
    val onClickDetail: ((cvID: Int) -> Unit)
}

class PostAdapter(val action: IPostAction) :
    PageRecyclerAdapter<RecruitmentForm, ItRvPostStaffBinding>(action) {

    override fun onBindHolder(
        item: RecruitmentForm, binding: ItRvPostStaffBinding, adapterPosition: Int
    ) {
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
        get() = R.layout.it_rv_post_staff
}