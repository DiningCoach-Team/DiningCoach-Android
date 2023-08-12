package com.diningcoach.data.model

import com.google.gson.annotations.SerializedName

data class AuthModel(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("userId")
    val userId: String? = null,
    @SerializedName("newUser")
    val newUser: Boolean? = null,
    @SerializedName("accessToken")
    val accessToken: String? = null,
    @SerializedName("refreshToken")
    val refreshToken: String? = null
)