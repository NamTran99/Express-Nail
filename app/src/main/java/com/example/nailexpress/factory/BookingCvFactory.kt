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
            price_display = skillDTO.price.safe().formatPrice(),
            isService = skillDTO.price != 0.0
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
            description = cvDTO.description.safe(),
            isShowDes = cvDTO.description.isNullOrBlank().not(),
            phone = cvDTO.phone,
            listSkill = createListSkill(cvDTO.skills ?: listOf()),
            state = cvDTO.state,
            priceFormat = textFormatter.displaySalaryType(cvDTO.salary_type,cvDTO.price.safe(), cvDTO.unit.safe()),
            salaryType = cvDTO.salary_type.safe(),
            price = cvDTO.price,
            unit = cvDTO.unit,
            address = cvDTO.address,
            listSkillByService = createListSkill(cvDTO.skills.safe().filter { it.price.safe() > 0.0 } ),
            listSkillByTime = createListSkill(cvDTO.skills.safe().filter { it.price.safe() <= 0.0 } )
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
            salon_id = booking.salon_id,
            bookingIDDisplay = "#ID: ${booking.id}",
            contact_name = booking.contact_name.safe(),
            contact_phone = booking.contact_phone.formatPhoneUSCustom(),
            listSkill = createListSkill(booking.skills.safe()),
            bookingStatus = booking.status,
            appRole = role,
            displayTimeOrder = textFormatter.displayBookingTime(
                booking.booking_time,
                appRole = role
            ),
            workTime = booking.work_time,
            price = booking.price,
            unit = booking.unit,
            bookingTime = booking.booking_time,
            state = booking.state,
            city = booking.city,
            createTime = booking.created_at
        )
    }

    fun createListBooking(booking: List<BookingDTO>): List<Booking> {
        return booking.map { createBooking(it, AppConfig.AppRole.Customer) }
    }
}