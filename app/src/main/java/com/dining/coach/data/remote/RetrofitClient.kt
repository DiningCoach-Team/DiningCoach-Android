package com.dining.coach.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        private const val BASE_URL = "https://base.server.dev/"

        val INSTANCE: Retrofit by lazy {
            getInstance()
        }

        private fun getInstance(): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(NetworkCallAdapter.Factory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createClient())
            .build()


        private fun createClient(): OkHttpClient {
            val clientBuilder = OkHttpClient.Builder()

            clientBuilder.addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            )

            return clientBuilder.build()
        }
    }
}

