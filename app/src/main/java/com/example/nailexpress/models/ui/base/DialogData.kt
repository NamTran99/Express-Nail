package com.example.nailexpress.models.ui.base

import com.example.nailexpress.R
import com.example.nailexpress.extension.Callback

data class DialogData(
    var type: DialogType = DialogType.CONFIRM,
    var titleID: Int = R.string.btn_confirm,
    var messageID: Int = 0,
    var message: String = "",
    val isVisibleCancel: Boolean = false,
    val txtCancelId: Int = R.string.btn_cancel,
    val txtOkId: Int = R.string.btn_ok,
    var callback: Callback = null
){
    private fun buildError(): DialogData{
        type = DialogType.ERROR
        titleID = R.string.error
        return this
    }

    fun buildError(message: String): DialogData {
        this.buildError()
        this.message = message
        return this
    }

    fun buildError(titleID: Int, messageID: Int): DialogData{
        this.buildError()
        this.titleID = titleID
        this.messageID = messageID
        return this
    }

    fun setCallBack(callback: Callback): DialogData{
        this.callback = callback
        return this
    }
}
enum class DialogType{
    ERROR, CONFIRM
}