package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.nailexpress.app.BookingStatusDefine
import com.example.nailexpress.databinding.LayoutButtonDisplayWithStatusBookingBinding

class ButtonDisplayWithStatusBooking(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    private var binding: LayoutButtonDisplayWithStatusBookingBinding

    init {
        binding = LayoutButtonDisplayWithStatusBookingBinding.inflate(
            LayoutInflater.from(
                context
            ), this, true
        )

        binding.btnIHaveArrived.setOnClickListener {

        }
    }

    fun disPlayButtonWithStatus(status: Int) {
        with(binding) {
            llPending.isVisible = status == BookingStatusDefine.Pending.bookingStatus
            btnFinish.isVisible = status == BookingStatusDefine.Arrived.bookingStatus
            btnStartMoveRendezvous.isVisible = status == BookingStatusDefine.Accept.bookingStatus
            btnIHaveArrived.isVisible = status == BookingStatusDefine.StartMoving.bookingStatus
            llContact.isVisible = status == BookingStatusDefine.Accept.bookingStatus
                    || status == BookingStatusDefine.StartMoving.bookingStatus
                    || status == BookingStatusDefine.Arrived.bookingStatus
        }
    }

    fun setListener(status: Int?, action: IBookingStatusAction) {
        if (status == null) return
        with(binding) {
            btnRefuse.setOnClickListener { action.denied(status) }
            btnAccept.setOnClickListener { action.accept(status) }
            btnFinish.setOnClickListener { action.finish(status) }
            btnStartMoveRendezvous.setOnClickListener { action.startMoveToRendezvous(status) }
            btnIHaveArrived.setOnClickListener { action.iHaveArived(status) }
            ivMess.setOnClickListener { action.message() }
            ivCall.setOnClickListener { action.call() }
        }
    }
}

interface IBookingStatusAction {
    fun denied(id: Int)
    fun accept(id: Int)
    fun finish(id: Int)
    fun startMoveToRendezvous(id: Int)
    fun iHaveArived(id: Int)

    fun message()

    fun call()
}