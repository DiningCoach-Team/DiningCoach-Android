package com.diningcoach.data.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    val id: String? = null,
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("refresh_token")
    val refreshToken: String? = null
)
