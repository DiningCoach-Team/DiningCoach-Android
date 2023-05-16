package com.dining.coach.data.remote

import com.dining.coach.util.const.NETWORK_RSPN_200
import com.dining.coach.util.const.NETWORK_RSPN_400
import com.dining.coach.util.const.NETWORK_RSPN_402
import com.dining.coach.util.const.NETWORK_RSPN_403
import com.dining.coach.util.const.NETWORK_RSPN_404
import com.dining.coach.util.const.NETWORK_RSPN_409
import com.dining.coach.util.const.NETWORK_RSPN_500
import com.dining.coach.util.const.NETWORK_RSPN_501
import com.dining.coach.util.const.NETWORK_RSPN_502
import com.dining.coach.util.const.NETWORK_RSPN_503
import com.dining.coach.util.debug.INFO
import com.dining.coach.util.debug.N_INFO
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class NetworkCall<T : Any>(private val call: Call<T>, private val apiInfo: APIInfo) : Call<NetworkResponse<T>> {
    override fun enqueue(callback: Callback<NetworkResponse<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {

                N_INFO("RESPONSE NETWORK =======================================================")
                N_INFO("RESPOSNE API -> ${apiInfo.api}")

                when (response.code()) {
                    NETWORK_RSPN_200 -> {
                        if (response.isSuccessful) {
                            callback.onResponse(
                                this@NetworkCall,
                                Response.success(NetworkResponse.Success(apiInfo, response.body()))
                            )
                        } else {
                            callback.onResponse(
                                this@NetworkCall,
                                Response.success(
                                    NetworkResponse.returnError(apiInfo)
                                )
                            )
                        }
                    }

                    NETWORK_RSPN_400,
                    NETWORK_RSPN_402,
                    NETWORK_RSPN_403,
                    NETWORK_RSPN_404 -> {
                        // Server Error

                    }

                    NETWORK_RSPN_409 -> {
                        // Result Error (ErrorBody 참조)

                    }

                    NETWORK_RSPN_500,
                    NETWORK_RSPN_501,
                    NETWORK_RSPN_502,
                    NETWORK_RSPN_503 -> {

                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val response = when (t) {
                    is IOException -> NetworkResponse.NetworkError(apiInfo, t)
                    else -> NetworkResponse.Unexpected(apiInfo, t)
                }
                callback.onResponse(this@NetworkCall, Response.success(response))
            }
        })
    }

    override fun clone(): Call<NetworkResponse<T>> = NetworkCall(call.clone(), apiInfo)

    override fun execute(): Response<NetworkResponse<T>> {
        throw UnsupportedOperationException("unsupported method")
    }

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout {
        // TODO : 인터넷 체크 로직 넣어줘야함
        return call.timeout()
    }
}