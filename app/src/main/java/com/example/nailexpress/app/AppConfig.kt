package com.example.nailexpress.app

object AppConfig {
    val endpoint: String get() = "https://dev.api.nails-express.kendemo.com/api/v1/"

    const val otpTimeout = 5
    const val perPage = 10
    const val timeSearch = 500L //ms

    const val REQUEST_CODE_SHARE_APP = 99


    enum class AppRole(val  id: Int) {
        Customer(1), Staff(0)
    }

    /*
    * READ: dành cho load ảnh
    * UPDATE: Dành cho upload ảnh
    * */
    enum class Status(val inx: Int){
        READ(0), UPDATE(1);
        companion object{
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

object PriceUnit{
    const val HOUR = 1
    const val DAY = 2
    const val WEEK = 3
    const val MONTH = 4
    const val YEAR = 5
}

object SKillType{
    const val SKill = 1
    const val Time = 2
}

// Recruitment
object SalaryType{
    const val Both = 0
    const val Service = 1
    const val Time = 2
}

object RecruitmentStatus {
    const val New = 1 // Mới
    const val Have_One = 2 // có người ứng tuyển
    const val Destroy = 3 // Hủy
}

