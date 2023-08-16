package com.example.nailexpress.app

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.nailexpress.R

fun AppConfig.AppRole?.safe() = this ?: AppConfig.AppRole.Customer

object AppConfig {
    val endpoint: String get() = "https://dev.api.nails-express.kendemo.com/api/v1/"

    const val otpTimeout = 5
    const val perPage = 10
    const val timeSearch = 500L //ms

    const val TIME_DEFAULT = "08:00:00"

    const val REQUEST_CODE_SHARE_APP = 99


    enum class AppRole(val id: Int) {
        Customer(1), Staff(0)
    }

    /*
    * READ: dành cho load ảnh
    * UPDATE: Dành cho upload ảnh
    * */
    enum class Status(val inx: Int) {
        READ(0), UPDATE(1);

        companion object {
            fun getDayByIndex(index: Int): Status {
                Status.values().forEach {
                    if (it.inx == index) return it
                }
                return Status.READ
            }
        }
    }
}

object Gender {
    const val Female = 0
    const val Male = 1
}

object WorkType {
    const val XuyenBang = 0
    const val NoXuyenBang = 1
}

object PriceUnit {
    const val HOUR = 1
    const val DAY = 2
    const val WEEK = 3
    const val MONTH = 4
    const val YEAR = 5
}

object SKillType {
    const val SKill = 1
    const val Time = 2
}

// Recruitment
//object SalaryType {
//    const val Both = 0
//    const val Service = 1
//    const val Time = 2
//}

enum class SalaryType(val data: Int) {
    Both(0), Service(1), Time(2);

    companion object {
        fun getSalaryType(data: Int): SalaryType {
            SalaryType.values().forEach {
                if (it.data == data) return it
            }
            return SalaryType.Service
        }
    }
}

object RecruitmentStatus {
    const val New = 1 // Mới
    const val Have_One = 2 // có người ứng tuyển
    const val Destroy = 3 // Hủy
}

object BookingStatus {
    const val Pending = 0
    const val Accept = 1
    const val Cancel = 2
    const val Expired = 3
}

enum class BookingStatusDefine(
    val bookingStatus: Int,
    val bookingStringRes: Int,
    @ColorRes val color: Int,
    @StringRes val positiveStringRes: Int = R.string.empty
) {
    Pending(0, R.string.lbl_pending, R.color.hED970E, R.string.accept),
    Accept(1, R.string.lbl_worker_accept, R.color.h0089FF, R.string.start_go_to_meeting_place),
    Deny(2, R.string.lbl_customer_has_mobilized, R.color.hED1B1B),
    Expires(3, R.string.lbl_expired_1, R.color.h909090),
    StartMoving(4, R.string.lbl_start_moving, R.color.hFF662E, R.string.i_have_arrived),
    Arrived(5, R.string.lbl_arrived, R.color.h0CD3B4, R.string.lbl_finish),
    Finish(6, R.string.lbl_finish, R.color.h29DB12)
}

enum class BookingStatusCustomerDefine(
    val bookingStatus: Int,
    val bookingStringRes: Int,
    @ColorRes val color: Int,
) {
    Pending(0, R.string.lbl_pending_customer, R.color.hED970E),
    Accept(1, R.string.lbl_worker_accept, R.color.h0089FF),
    Deny(2, R.string.lbl_master_refused, R.color.hED1B1B),
    Expires(3, R.string.lbl_expired_1, R.color.h909090),
    StartMoving(4, R.string.lbl_start_moving, R.color.hFF662E),
    Arrived(5, R.string.lbl_arrived, R.color.h0CD3B4),
    Finish(6, R.string.lbl_finish, R.color.h29DB12)
}