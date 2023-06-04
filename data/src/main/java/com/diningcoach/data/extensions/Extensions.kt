package com.diningcoach.data.extensions

import com.diningcoach.data.model.UserModel
import com.diningcoach.domain.model.User

fun UserModel.toUser(): User {
    return User(
        id,  accessToken, refreshToken
    )
}
