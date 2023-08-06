package com.example.nailexpress.repository

import android.content.Context
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.datasource.remote.SalonApi
import com.example.nailexpress.extension.buildMultipart
import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.example.nailexpress.extension.filterRemoteImage
import com.example.nailexpress.extension.toArrayPart
import com.example.nailexpress.factory.SalonFactory
import com.example.nailexpress.helper.RequestBodyBuilder
import com.example.nailexpress.models.ui.main.Salon
import kotlinx.coroutines.flow.flow

class SalonRepository(
    val userDataSource: SharePrefs,
    val context: Context,
    val salonApi: SalonApi,
    val salonFactory: SalonFactory
) {
    suspend fun createSalon(form: Salon) = flow {
        form.validate()
        val imageParts =
            form.localImage.filterRemoteImage().toArrayPart("salon_images[]")

        emit(
            salonFactory.createASalon(
                salonApi.createSalon(
                    RequestBodyBuilder()
                        .put("name", form.name)
                        .put("experience_years", form.experience_years)
                        .put("have_place", form.have_place)
                        .put("have_car", form.have_car)
                        .put("customer_skin_color", form.customer_skin_color)
                        .put("phone", form.phoneDisplay.convertPhoneToNormalFormat())
                        .put("description", form.description)
                        .put("address", form.address)
                        .put("latitude", form.latitude)
                        .put("longitude", form.longitude)
                        .put("city", form.city)
                        .put("state", form.state)
                        .put("zipcode", form.zipcode)
                        .put("create_type", 1) //create salon 1,
                        .buildMultipart(), imageParts
                ).await()
            )
        )
    }


    fun getSalonDetail() = flow {
        emit(
            salonFactory.createListSalon(
                salonApi.getMySalon().await()
            )
        )
    }

    fun getSalonByID(id: Int) = flow {
        emit(
            salonFactory.createASalon(
                salonApi.getSalonById(id).await()
            )
        )
    }


}