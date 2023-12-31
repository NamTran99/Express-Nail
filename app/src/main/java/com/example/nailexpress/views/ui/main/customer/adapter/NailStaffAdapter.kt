package com.example.nailexpress.views.ui.main.customer.adapter

import android.annotation.SuppressLint
import android.support.core.view.ILoadMoreAction
import com.example.nailexpress.R
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.databinding.ItemListStaffBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.safe
import com.example.nailexpress.factory.TextFormatter
import com.example.nailexpress.models.ui.main.Cv

interface INailStaffAction : ILoadMoreAction {
    val onClickBookStaff: ((cvID: Int) -> Unit)
    val onClickViewDetail: ((id: Int) -> Unit)
}

class NailStaffAdapter(val action: INailStaffAction) :
    PageRecyclerAdapter<Cv, ItemListStaffBinding>(action) {

    @SuppressLint("SetTextI18n")
    override fun onBindHolder(item: Cv, binding: ItemListStaffBinding, adapterPosition: Int) {
        binding.apply {
            imgImage.setImageUrl(item.avatar)
            tvNameGender.text = "${item.name} ${item.genderFormat2}"
            tvWorkplace.text = item.state
            tvWorkingType.text = item.workTypeDisplay
            tvDistance.text = item.distanceFormat
            btViewDetail.onClick {
                action.onClickViewDetail.invoke(item.id)
            }
            btBookStaff.onClick {
                action.onClickBookStaff.invoke(item.id)
            }
            tvSalary.text =
                TextFormatter(root.context).displaySalaryType(
                    item.salaryType,
                    item.price.safe(),
                    item.unit
                )

        }
    }

    override val layoutId: Int
        get() = R.layout.item_list_staff
}