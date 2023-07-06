package com.example.nailexpress.views.dialog.loading

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseDialog

class LoadingDialog(context: Context, owner: LifecycleOwner? = null) : BaseDialog(context),
    LifecycleEventObserver {

    private var mView: RotateLoadingView? = null
    private var mShowing: Boolean = false

    init {
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
        mView = findViewById(R.id.spin_loading)
        val window = window!!
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        when (owner) {
            is Fragment -> owner.viewLifecycleOwner.lifecycle
            is Activity -> owner.lifecycle
            else -> null
        }?.addObserver(this)
    }

    fun show(it: Boolean) {
        if (it) show() else dismiss()
        mShowing = it
    }

    override fun show() {
        super.show()
        if (mView != null) {
            mView!!.start()
        }
    }

    override fun hide() {
        if (mView != null)
            mView!!.stop()
        super.hide()
    }

    override fun dismiss() {
        if (mView != null)
            mView!!.stop()
        super.dismiss()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> dismiss()
            Lifecycle.Event.ON_STOP -> dismiss()
            Lifecycle.Event.ON_START -> if (mShowing) show()
            else -> {}
        }
    }
}
