package com.example.nailexpress.views.ui.main.staff.adapter

import android.support.core.view.ILoadMoreAction
import androidx.core.view.isVisible
import com.example.nailexpress.R
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.databinding.ItRvPostStaffBinding
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.extension.getString
import com.example.nailexpress.extension.safe
import com.example.nailexpress.extension.setImageURICustom
import com.example.nailexpress.factory.TextFormatter
import com.example.nailexpress.models.ui.main.RecruitmentForm
import com.example.nailexpress.models.ui.main.User

interface IPostAction : ILoadMoreAction {
    val onClickDetail: ((cvID: Int) -> Unit)
}

class PostAdapter(val action: IPostAction) :
    PageRecyclerAdapter<RecruitmentForm, ItRvPostStaffBinding>(action) {

    override fun onBindHolder(
        item: RecruitmentForm, binding: ItRvPostStaffBinding, adapterPosition: Int
    ) {
        binding.apply {
            val user =  SharePrefs.getInstance(binding.root.context).get<User>(SharePrefKey.USER_DTO) ?: return
            data = item

            item.image_url?.let {
                ivLogo.setImageURICustom(it)
            }

            btnDetail.setOnClickListener {
                item.id?.let {
                        it1 -> action.onClickDetail(it1)
                }
            }

            tvPost.isVisible = user.id == item.user_id
            tvId.text = root.getString(R.string.lbl_id,item.id.safe())
            textFormat = TextFormatter(root.context)
        }
    }

    override val layoutId: Int
        get() = R.layout.it_rv_post_staff
}