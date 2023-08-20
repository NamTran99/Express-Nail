package com.example.nailexpress.models.ui.main

import com.example.nailexpress.views.ui.main.profile.adapters.ISelector


data class Skill(
    var id: Int = 0,
    var name: String = "",
    var price: Double = 0.0,
    //custom
    var price_display: String = "",
    var isService: Boolean = true, override var isCheck: Boolean = false,
):ISelector<Skill>{
    override fun setIsCheck(cur: Skill): ISelector<Skill> {
        return this
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}