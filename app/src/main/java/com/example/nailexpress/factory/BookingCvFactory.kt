package com.example.nailexpress.factory

import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.example.nailexpress.extension.formatPhoneUS
import com.example.nailexpress.extension.formatPhoneUSCustom
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.response.BookingDTO
import com.example.nailexpress.models.response.CvDTO
import com.example.nailexpress.models.response.SkillDTO
import com.example.nailexpress.models.ui.main.Booking
import com.example.nailexpress.models.ui.main.Cv
import com.example.nailexpress.models.ui.main.ISkill


class BookingCvFactory(val textFormatter: TextFormatter) {


    fun createASkill(skillDTO: SkillDTO): ISkill {
        return object : ISkill {
            override val name: String
                get() = skillDTO.name.let { if(it.isNotEmpty()) it else skillDTO.custom_skill}
            override val id: Int
                get() = skillDTO.id
            override val price: Double
                get() = skillDTO.price.safe()
            override val priceDisplay: String
                get() = textFormatter.formatPrice(price)
        }
    }

    fun createListSkill(listItem: List<SkillDTO>): List<ISkill> {
        return listItem.map(this::createASkill)
    }

    fun createCvFactory(cvDTO: CvDTO): Cv {
        return Cv(
            name = cvDTO.fullname.safe(),
            genderFormat = textFormatter.displayGender(cvDTO.gender),
            genderFormat2 = "(${textFormatter.displayGender(cvDTO.gender)})",
            distance = cvDTO.distance,
            distanceFormat = textFormatter.displayDistance(cvDTO.distance),
            workTypeDisplay = textFormatter.displayWorkType(cvDTO.work_type),
            workingPlace = cvDTO.work_place.safe(),
            avatar = cvDTO.avatar_url.safe(),
            id = cvDTO.id,
            year_exper = cvDTO.experience_years,
            year_exper_display = textFormatter.displayYearExper(cvDTO.experience_years),
            description = cvDTO.description,
            phone = cvDTO.phone.formatPhoneUSCustom(),
            listSkill = createListSkill(cvDTO.skills ?: listOf()),
            state = cvDTO.state
        )
    }


    fun createListCv(list: List<CvDTO>): List<Cv> {
        return list.map(this::createCvFactory)
    }

    fun createBooking(booking: BookingDTO): Booking {
        return Booking(
            bookingID = booking.id,
            cv = createCvFactory(booking.cv),
            status_booking_display = textFormatter.displayStatusBooking(booking.status),
            colorStatus = textFormatter.displayStatusColor(booking.status),
            salon_id = booking.salon_id,
            bookingIDDisplay = "#ID: ${booking.id}",
            contact_name = booking.contact_name.safe(),
            contact_phone = booking.contact_phone.formatPhoneUSCustom(),
            listSkill = createListSkill(booking.skills.safe())
        )
    }

    fun createListBooking(booking: List<BookingDTO>): List<Booking> {
        return booking.map(this::createBooking)
    }
}
