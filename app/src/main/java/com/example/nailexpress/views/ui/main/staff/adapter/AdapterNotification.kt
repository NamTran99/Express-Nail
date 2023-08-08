package com.example.nailexpress.views.ui.main.staff.adapter

import android.support.core.view.ILoadMoreAction
import com.example.nailexpress.R
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.databinding.ItNotificationBinding
import com.example.nailexpress.extension.Format.FORMAT_DATE_UTC
import com.example.nailexpress.extension.Format.FORMAT_TIME_1
import com.example.nailexpress.extension.convertTime
import com.example.nailexpress.models.response.NotificationResponse

interface IActionNotification : ILoadMoreAction{
    open fun onClickDetail()
}
class AdapterNotification(val action: IActionNotification) : PageRecyclerAdapter<NotificationResponse,ItNotificationBinding>(action) {
    override val layoutId = R.layout.it_notification

    override fun onBindHolder(
        item: NotificationResponse,
        binding: ItNotificationBinding,
        adapterPosition: Int
    ) {
        with(binding){
            notify = item
            tvTime.text = item.createdAt?.convertTime(FORMAT_DATE_UTC,FORMAT_TIME_1)
            btBookStaff.setOnClickListener {
                action.onClickDetail()
            }
        }
    }
}