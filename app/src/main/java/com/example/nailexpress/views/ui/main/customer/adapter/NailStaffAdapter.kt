package com.example.nailexpress.views.ui.main.customer.adapter

import android.support.core.view.ILoadMoreAction
import android.support.core.view.bindingOf
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.databinding.ItemListStaffBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.models.ui.main.Cv

interface INailStaffAction: ILoadMoreAction{
    val onClickBookStaff: ((cvID: Int) -> Unit)
    val onClickViewDetail : ((id:Int) -> Unit)
}
class NailStaffAdapter(val action: INailStaffAction) :
    PageRecyclerAdapter<Cv, ItemListStaffBinding>(action) {

    override fun onCreateBinding(parent: ViewGroup): ItemListStaffBinding {
        return parent.bindingOf(ItemListStaffBinding::inflate)
    }

    override fun onBindHolder(item: Cv, binding: ItemListStaffBinding, adapterPosition: Int) {
        binding.apply {
            imgImage.setImageUrl(item.avatar)
            tvName.text = item.name
            tvGender.text = item.genderFormat2
            tvWorkplace.text = item.state
            tvWorkingType.text = item.workTypeDisplay
            tvDistance.text = item.distanceFormat
            btViewDetail.onClick{
                action.onClickViewDetail.invoke(item.id)
            }
            btBookStaff.onClick{
                action.onClickBookStaff.invoke(item.id)
            }
        }
    }
}