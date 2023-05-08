package com.dining.coach.data.remote

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class NetworkCall<T : Any>(private val call: Call<T>) : Call<NetworkResponse<T>> {
    override fun enqueue(callback: Callback<NetworkResponse<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback.onResponse(
                        this@NetworkCall,
                        Response.success(NetworkResponse.Success(response.body()))
                    )
                } else {
                    callback.onResponse(
                        this@NetworkCall,
                        Response.success(
                            NetworkResponse.Failure(
                                response.code(),
                                response.errorBody()?.string()
                            )
                        )
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val response = when (t) {
                    is IOException -> NetworkResponse.NetworkError(t)
                    else -> NetworkResponse.Unexpected(t)
                }
                callback.onResponse(this@NetworkCall, Response.success(response))
            }
        })
    }

    override fun clone(): Call<NetworkResponse<T>> = NetworkCall(call.clone())

    override fun execute(): Response<NetworkResponse<T>> {
        throw UnsupportedOperationException("unsupported method")
    }

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()
}