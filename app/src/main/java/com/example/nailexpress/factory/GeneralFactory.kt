package com.example.nailexpress.factory

import com.example.nailexpress.app.SKillType
import com.example.nailexpress.extension.formatPrice
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.response.SkillDTO
import com.example.nailexpress.models.ui.main.Skill


class GeneralFactory(val textFormatter: TextFormatter) {


    private fun createAService(serviceDTO: SkillDTO): Skill {
        return Skill(
            id = serviceDTO.id.safe(),
            name = serviceDTO.name.safe(),
            isService = serviceDTO.type.safe() == SKillType.SKill, //NOTE 1
            price_display = serviceDTO.price.safe().formatPrice()
        )
    }

    fun createListService(listItem: List<SkillDTO>): List<Skill> {
        return listItem.map(this::createAService)
    }
}