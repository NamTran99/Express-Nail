package com.example.nailexpress.factory

import com.example.nailexpress.extension.formatPhoneUSCustom
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.response.ImageDTO
import com.example.nailexpress.models.response.SalonDTO
import com.example.nailexpress.models.ui.AppImage
import com.example.nailexpress.models.ui.main.Salon


class SalonFactory(val textFormatter: TextFormatter) {

    fun createASalon(salon: SalonDTO): Salon {
        return Salon(
            name = salon.name.safe(),
            have_car = salon.have_car.safe(),
            have_place = salon.have_place.safe(),
            experience_years = salon.experience_years.toString(),
            customer_skin_color = salon.customer_skin_color.safe(),
            phoneDisplay = salon.phone.safe().formatPhoneUSCustom(),
            description = salon.description.safe(),
            latitude = salon.latitude.safe(),
            longitude = salon.longitude.safe(),
            city = salon.city.safe(),
            state = salon.state.safe(),
            zipcode = salon.zipcode.safe(),
            skinColorDisplay = textFormatter.customerSkinColorFormat(salon.customer_skin_color.safe()),
            experience_years_display = textFormatter.displayYearExper(salon.experience_years.safe()),
            display_have_car = textFormatter.displayYesNo(salon.have_car.safe()),
            display_have_place = textFormatter.displayYesNo(salon.have_place.safe()),
            address = salon.address.safe(),
            listImage = createListImage(salon.images?: listOf()),
            salonID = salon.id.safe(),
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