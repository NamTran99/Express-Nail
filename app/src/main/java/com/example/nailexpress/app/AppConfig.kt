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

