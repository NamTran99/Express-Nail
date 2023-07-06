package com.example.nailexpress.factory

import com.example.nailexpress.extension.formatPhoneUS
import com.example.nailexpress.extension.formatPhoneUSCustom
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.response.ImageDTO
import com.example.nailexpress.models.response.SalonDTO
import com.example.nailexpress.models.ui.AppImage
import com.example.nailexpress.models.ui.main.Salon


class SalonFactory(val textFormatter: TextFormatter) {

    fun createASalon(salon: SalonDTO): Salon {
        return Salon(
            name = salon.name,
            have_car = salon.have_car,
            have_place = salon.have_place,
            experience_years = salon.experience_years.toString(),
            customer_skin_color = salon.customer_skin_color,
            phoneDisplay = salon.phone.formatPhoneUSCustom(),
            description = salon.description.safe(),
            latitude = salon.latitude,
            longitude = salon.longitude,
            city = salon.city,
            state = salon.state,
            zipcode = salon.zipcode.toIntOrNull().safe(),
            skinColorDisplay = textFormatter.customerSkinColorFormat(salon.customer_skin_color),
            experience_years_display = textFormatter.displayYearExper(salon.experience_years),
            display_have_car = textFormatter.displayYesNo(salon.have_car),
            display_have_place = textFormatter.displayYesNo(salon.have_place),
            address = salon.address,
            listImage = createListImage(salon.images?: listOf()),
            salonID = salon.id,
            isImageEmpty = salon.images.safe().isEmpty()
        )
    }

    fun createListSalon(listItem: List<SalonDTO>): List<Salon> {
        return listItem.map(this::createASalon)
    }

    fun createAImage(image: ImageDTO): AppImage{
        return  AppImage(
            id = image.id,
            image = image.image_url
        )
    }

    fun createListImage(listImage: List<ImageDTO>): List<AppImage>{
        return  listImage.map (this::createAImage)
    }
}