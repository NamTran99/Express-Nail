package com.example.nailexpress.views.ui.main.staff.adapter

import android.support.core.view.ILoadMoreAction
import com.example.nailexpress.R
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.databinding.ItRvBookingOfMeBinding
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.factory.TextFormatter
import com.example.nailexpress.models.response.BookingDTO
import com.example.nailexpress.models.ui.main.User

interface IBookingOfMeAction : ILoadMoreAction {
    fun denied(id: Int)
    fun accept(id: Int)
    fun finish(id: Int)
    fun startMoveToRendezvous(id: Int)

    fun iHaveArived(id: Int)

    fun message(phone: String)

    fun call(phone: String)
}

class BookingOfMeAdapter(private val action: IBookingOfMeAction) :
    PageRecyclerAdapter<BookingDTO, ItRvBookingOfMeBinding>(action) {
    override val layoutId = R.layout.it_rv_booking_of_me

    override fun onBindHolder(
        item: BookingDTO,
        binding: ItRvBookingOfMeBinding,
        adapterPosition: Int
    ) {
        with(binding) {
            textFormat = TextFormatter(binding.root.context)
            data = item
            action = this@BookingOfMeAdapter.action
        }
    }
}