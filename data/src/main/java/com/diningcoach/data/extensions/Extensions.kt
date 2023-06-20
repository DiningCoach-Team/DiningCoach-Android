package com.diningcoach.data.extensions

import com.diningcoach.data.model.PhotoModel
import com.diningcoach.data.model.UserModel
import com.diningcoach.domain.model.Photo
import com.diningcoach.domain.model.User
import java.util.*

fun UserModel.toUser(): User {
    return User(
        id, accessToken, refreshToken
    )
}

fun PhotoModel.toPhoto() = Photo(
    uri,
    name,
    fullName,
    mimeType,
    Date(addedDate),
    folder,
    size,
    width,
    height,
)
