package com.dining.coach.data.remote

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkCallAdapter<R: Any>(private val responseType: Type): CallAdapter<R, Call<NetworkResponse<R>>> {
    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): Call<NetworkResponse<R>> {
        return NetworkCall(call)
    }

    class Factory: CallAdapter.Factory() {
        override fun get(
            returnType: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ): CallAdapter<*, *>? {
            if (getRawType(returnType) != Call::class.java) {
                return null
            }

            check(returnType is ParameterizedType) {

            }

            val responseType = getParameterUpperBound(0, returnType)

            if (getRawType(responseType) != NetworkResponse::class.java) {
                return null
            }

            check(responseType is ParameterizedType) {

            }

            val successBodyType = getParameterUpperBound(0, responseType)

            return NetworkCallAdapter<Any>(successBodyType)
        }
    }
}