package com.example.nailexpress.views.dialog

import android.content.Context
import android.support.core.view.ViewScopeOwner
import com.example.nailexpress.base.BaseDialog
import com.example.nailexpress.databinding.DialogMessageBinding
import com.example.nailexpress.extension.hide
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.show
import com.example.nailexpress.models.ui.base.DialogData
import com.example.nailexpress.models.ui.base.DialogType

class CommonDialog(context: Context) : BaseDialog(context) {
    private val binding = viewBinding(DialogMessageBinding::inflate)

    init {
        binding.btCancel.setOnClickListener {
            dismiss()
        }
    }

    fun show(data: DialogData) {
        binding.apply {
            data.apply {
                btCancel.show(isVisibleCancel)
                when(data.type){
                    DialogType.ERROR ->{
                        btConfirm.hide()
                        btYes.show()
                    }
                    DialogType.CONFIRM -> {
                        btConfirm.show()
                        btYes.hide()
                    }
                }
                txtTitle.setText(data.titleID)
                txtBody.setText(data.message)
                btYes.onClick{
                    dismiss()
                    callback?.invoke()
                }
                btConfirm.onClick{
                    dismiss()
                    callback?.invoke()
                }
            }
        }
        super.show()
    }
    override fun show() {
        error("Not support")
    }
}
interface CommonDialogOwner : ViewScopeOwner {
    val commonDialog: CommonDialog
        get() = with(viewScope) {
            getOr("common:dialog") { CommonDialog(context) }
        }
}