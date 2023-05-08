package com.dining.coach.data.remote

import java.io.IOException

sealed class NetworkResponse<out T : Any> {
    data class Success<T : Any>(val body: T?) : NetworkResponse<T>()

    data class Failure<T: Any>(val code: Int, val error: String?) : NetworkResponse<T>()

    data class NetworkError(val error: IOException) : NetworkResponse<Nothing>()

    data class Unexpected(val t: Throwable) : NetworkResponse<Nothing>()
}