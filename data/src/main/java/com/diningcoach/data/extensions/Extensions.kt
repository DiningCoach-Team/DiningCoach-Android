package com.diningcoach.data.extensions

import com.diningcoach.data.model.GalleryModel
import com.diningcoach.data.model.UserModel
import com.diningcoach.domain.model.Gallery
import com.diningcoach.domain.model.User
import java.util.*

fun UserModel.toUser(): User {
    return User(
        id, accessToken, refreshToken
    )
}

fun GalleryModel.toGallery() = Gallery(
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
