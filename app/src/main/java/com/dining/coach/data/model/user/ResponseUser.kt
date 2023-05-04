package com.dining.coach.data.model.user

import com.google.gson.annotations.SerializedName

data class ResponseUser(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("accessTkn")
    val accessTkn: String? = null,
    @SerializedName("refreshTkn")
    val refreshTkn: String? = null
) {
    fun ResponseUser.toLocal(): User {
        return User(
            id = id,
            accessTkn = accessTkn,
            refreshTkn = refreshTkn
        )
    }
}