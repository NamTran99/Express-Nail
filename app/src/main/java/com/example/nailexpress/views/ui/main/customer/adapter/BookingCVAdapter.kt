package com.example.nailexpress.views.ui.main.customer.adapter

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.core.view.ILoadMoreAction
import androidx.core.content.ContextCompat
import com.example.nailexpress.R
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.databinding.ItemListBookingBinding
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.safe
import com.example.nailexpress.factory.TextFormatter
import com.example.nailexpress.models.ui.main.Booking

interface IBookingCVAction : ILoadMoreAction {
    val onClickViewDetailBooking: ((id: Int) -> Unit)
    fun clickOpenMess(phone: String)
    fun clickOpenCall(phone: String)
}

class BookingCVAdapter(val action: IBookingCVAction) :
    PageRecyclerAdapter<Booking, ItemListBookingBinding>(action) {


    @SuppressLint("ResourceAsColor")
    override fun onBindHolder(
        item: Booking,
        binding: ItemListBookingBinding,
        adapterPosition: Int
    ) {
        binding.apply {
            data = item
            val cv = item.cv
            imgImage.setImageUrl(cv.avatar)
            tvStatus.compoundDrawablesRelative[0].colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(
                    binding.root.context,
                    item.colorStatus
                ), PorterDuff.Mode.SRC_IN
            )
            btBookStaff.onClick {
                action.onClickViewDetailBooking.invoke(item.bookingID)
            }

            val formater = TextFormatter(root.context)
            textFormater = formater
            tvSalary.text = formater.displaySalaryType(
                cv.salaryType,
                item.price.safe(),
                item.unit
            )

            ivCall.setOnClickListener {
                action.clickOpenCall(item.contact_phone)
            }

            ivMess.setOnClickListener {
                action.clickOpenMess(item.contact_phone)
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.item_list_booking
}