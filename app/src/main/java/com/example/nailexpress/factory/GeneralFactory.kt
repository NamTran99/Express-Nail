package com.example.nailexpress.factory

import com.example.nailexpress.extension.formatPhoneUS
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.response.CvDTO
import com.example.nailexpress.models.response.ServiceDTO
import com.example.nailexpress.models.response.SkillDTO
import com.example.nailexpress.models.ui.main.Cv
import com.example.nailexpress.models.ui.main.ISkill
import com.example.nailexpress.models.ui.main.Service


class GeneralFactory(val textFormatter: TextFormatter) {


    fun createAService(serviceDTO: ServiceDTO): Service {
       return Service(
           id = serviceDTO.id,
           name = serviceDTO.name
       )
    }

    fun createListService(listItem: List<ServiceDTO>): List<Service>{
        return listItem.map(this::createAService)
    }


}