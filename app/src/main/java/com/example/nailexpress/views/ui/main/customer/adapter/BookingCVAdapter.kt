package com.example.nailexpress.views.ui.main.customer.adapter

import android.annotation.SuppressLint
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.core.view.ILoadMoreAction
import android.support.core.view.bindingOf
import android.view.ViewGroup
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
            val cv = item.cv
            tvStatus.text = item.status_booking_display
            imgImage.setImageUrl(cv.avatar)
            tvId.text = item.bookingIDDisplay
            tvStatus.compoundDrawablesRelative[0].colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(
                    binding.root.context,
                    item.colorStatus
                ), PorterDuff.Mode.SRC_IN
            )
            tvName.text = cv.name
            tvGender.text = cv.genderFormat2
            tvState.text = cv.state
            tvWorkingType.text = cv.workTypeDisplay
            tvDistance.text = cv.distanceFormat
            btBookStaff.onClick {
                action.onClickViewDetailBooking.invoke(item.bookingID)
            }

            tvSalary.text =
                TextFormatter(root.context).displaySalaryType(
                    cv.salaryType,
                    item.price.safe(),
                    item.unit
                )
        }
    }

    override val layoutId: Int
        get() = R.layout.item_list_booking
}