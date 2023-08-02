package com.example.nailexpress.factory

import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.extension.formatPhoneUSCustom
import com.example.nailexpress.extension.formatPrice
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.response.BookingDTO
import com.example.nailexpress.models.response.CvDTO
import com.example.nailexpress.models.response.SkillDTO
import com.example.nailexpress.models.ui.main.Booking
import com.example.nailexpress.models.ui.main.Cv
import com.example.nailexpress.models.ui.main.Skill


class BookingCvFactory(val textFormatter: TextFormatter) {

    fun createASkill(skillDTO: SkillDTO): Skill {
        return Skill(
            name = skillDTO.name?.let { it.ifEmpty { skillDTO.custom_skill } }.safe(),
            id = skillDTO.id.safe(),
            price = skillDTO.price.safe(),
            price_display = skillDTO.price.safe().formatPrice()
        )
    }


    fun createListSkill(listItem: List<SkillDTO>): List<Skill> {
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
            state = cvDTO.state,
            priceFormat = textFormatter.displaySalary(cvDTO.price.safe(), cvDTO.unit.safe()),
            isSkillEmpty = cvDTO.skills.safe().isEmpty(),
            salaryType = cvDTO.salary_type.safe()
        )
    }


    fun createListCv(list: List<CvDTO>): List<Cv> {
        return list.map(this::createCvFactory)
    }

    fun createBooking(booking: BookingDTO, role: AppConfig.AppRole): Booking {
        return Booking(
            bookingID = booking.id,
            cv = createCvFactory(booking.cv),
            status_booking_display = textFormatter.displayStatusBooking(booking.status),
            colorStatus = textFormatter.displayStatusColor(booking.status),
            salon_id = booking.salon_id.safe(),
            bookingIDDisplay = "#ID: ${booking.id}",
            contact_name = booking.contact_name.safe(),
            contact_phone = booking.contact_phone.formatPhoneUSCustom(),
            listSkill = createListSkill(booking.skills.safe()),
            bookingStatus = booking.status,
            appRole = role,
            displayTimeOrder = textFormatter.displayBookingTime(booking.booking_time, appRole = role)
        )
    }

    fun createListBooking(booking: List<BookingDTO>): List<Booking> {
        return booking.map{createBooking(it, AppConfig.AppRole.Customer)}
    }
}