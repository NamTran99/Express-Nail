package com.example.nailexpress.views.dialog

import android.content.Context
import android.support.core.view.ViewScopeOwner
import androidx.annotation.StringRes
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseDialog
import com.example.nailexpress.databinding.DialogNoticeConfirmBinding
import com.example.nailexpress.extension.show


class ConfirmNoticeDialog(context: Context) : BaseDialog(context) {
    private val binding = viewBinding(DialogNoticeConfirmBinding::inflate)

    init {
        setCancelable(false)
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    fun show(
        title: String,
        message: String,
        function: () -> Unit = {}
    ) {
        binding.txtTitle.text = title
        binding.txtBody.text = message
        binding.btnDismiss.setOnClickListener {
            function()
            dismiss()
        }
        super.show()
    }

    fun show(
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes buttonConfirm: Int = R.string.btn_confirm,
        isShowCancel: Boolean = true,
        function: () -> Unit = {}
    ) {
        isShowCancel.show(binding.btnCancel)
        binding.txtTitle.setText(title)
        binding.txtBody.setText(message)
        binding.btnDismiss.setText(buttonConfirm)
        binding.btnDismiss.setOnClickListener {
            function()
            dismiss()
        }
        super.show()
    }

    fun show(
        @StringRes title: Int,
        message: String,
        @StringRes buttonConfirm: Int = R.string.btn_confirm,
        isShowCancel: Boolean = true,
        function: () -> Unit = {}
    ) {
        isShowCancel.show(binding.btnCancel)
        binding.txtTitle.setText(title)
        binding.txtBody.text = message
        binding.btnDismiss.setText(buttonConfirm)
        binding.btnDismiss.setOnClickListener {
            function()
            dismiss()
        }
        super.show()
    }

    fun show(
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes buttonConfirm: Int = R.string.btn_confirm,
        isShowCancel: Boolean = true,
        functionSubmit: () -> Unit = {},
        functionCancel: () -> Unit = {},
    ) {
        isShowCancel.show(binding.btnCancel)
        binding.txtTitle.setText(title)
        binding.txtBody.setText(message)
        binding.btnDismiss.setText(buttonConfirm)
        binding.btnCancel.setOnClickListener{
            functionCancel()
            dismiss()
        }
        binding.btnDismiss.setOnClickListener {
            functionSubmit()
            dismiss()
        }
        super.show()
    }

    fun showCallPhone(
        title: String,
        message: String,
        function: () -> Unit = {}
    ) {
        binding.txtTitle.text = title
        binding.txtBody.text = message
        binding.btnDismiss.setText(R.string.btn_confirm_call)
        binding.btnDismiss.setOnClickListener {
            function()
            dismiss()
        }
        super.show()
    }

    fun showDelete(
        @StringRes title: Int,
        message: String,
        function: () -> Unit = {}
    ) {
        binding.txtTitle.setText(title)
        binding.txtBody.text = message
        binding.btnDismiss.setText(R.string.btn_yes_delete)
        binding.btnDismiss.setOnClickListener {
            function()
            dismiss()
        }
        super.show()
    }

    fun showDelete(
        @StringRes title: Int,
        @StringRes message: Int,
        function: () -> Unit = {}
    ) {
        binding.txtTitle.setText(title)
        binding.txtBody.setText(message)
        binding.btnDismiss.setText(R.string.btn_yes_delete)
        binding.btnDismiss.setOnClickListener {
            function()
            dismiss()
        }
        super.show()
    }
}

interface ConfirmDialogOwner : ViewScopeOwner {
    val confirmDialog: ConfirmNoticeDialog
        get() = with(viewScope) {
            getOr("confirm:dialog") { ConfirmNoticeDialog(context) }
        }
}