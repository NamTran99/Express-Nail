package com.example.nailexpress.views.ui.main.profile.adapters

import androidx.annotation.StringRes
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.SimpleSelectorRecyclerAdapter
import com.example.nailexpress.databinding.ItemSelectRoleBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.show

interface ISelectRoleAdapterCallBack{
    val onItemSelect: ((appRole: AppConfig.AppRole) -> Unit)
}

class SelectRoleAdapter(val action: ISelectRoleAdapterCallBack) :
    SimpleSelectorRecyclerAdapter<RoleOption, ItemSelectRoleBinding>() {

    override fun onBindHolder(
        item: RoleOption,
        binding: ItemSelectRoleBinding,
        adapterPosition: Int
    ) {
        binding.apply {
            tvName.setText(item.title)
            imgTick.show(item.isCheck)
            root.onClick {
                selectOne(item)
                action.onItemSelect(item.appRole)
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.item_select_role
}

interface ISelector<T>{
    var isCheck: Boolean
    fun setIsCheck(cur: T): ISelector<T>
}

data class RoleOption(
    @StringRes val title: Int= R.string.change_role,
    val appRole: AppConfig.AppRole = AppConfig.AppRole.Customer,
) : ISelector<AppConfig.AppRole> {

    override var isCheck = false

    override fun setIsCheck(cur: AppConfig.AppRole):RoleOption {
        isCheck = appRole == cur
        return this
    }
}