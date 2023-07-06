package com.example.nailexpress.factory

import com.example.nailexpress.extension.formatPhoneUS
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.response.UserDTO
import com.example.nailexpress.models.response.UserData
import com.example.nailexpress.models.ui.main.User


class ProfileFactory(val textFormatter: TextFormatter) {

    fun createAUserFromDTO(user: UserDTO): User {
        val data = user.user ?: UserData()
        return createAUser(data)
    }

    fun createAUser(data: UserData): User {
        return User(
            address = data.address.safe(),
            avatar_url = data.avatar_url.safe(),
            birthday = data.birthday.safe(),
            city = data.city.safe(),
            email = data.email.safe(),
            fullname = data.fullname.safe(),
            gender = data.gender.safe(),
            phoneDisplay = data.phone.safe().formatPhoneUS(),
            latitude = data.latitude.safe(),
            longitude = data.longitude.safe(),
            state = data.state.toString(),
            token = data.token.safe()
        )
    }
}