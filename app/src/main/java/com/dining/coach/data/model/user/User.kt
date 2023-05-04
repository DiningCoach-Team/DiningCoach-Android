package com.dining.coach.data.model.user

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser

data class User(
    val id: String? = null,
    val accessTkn: String? = null,
    val refreshTkn: String? = null
) {
    internal fun User.toDomain(): ResponseUser {
        return ResponseUser(
            id = id,
            accessTkn = accessTkn,
            refreshTkn = refreshTkn
        )
    }

    fun toJsonObject(): JsonObject {
        val string = Gson().toJson(this)
        return JsonParser.parseString(string).asJsonObject
    }
}
