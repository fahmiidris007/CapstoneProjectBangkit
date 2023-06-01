package com.c23ps266.capstoneprojectnew.data.remote.retrofit

import com.c23ps266.capstoneprojectnew.BuildConfig
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// if more than one BASE_URL are needed, this might help: https://stackoverflow.com/a/55265180
object ApiConfig {
    const val BASE_URL = "https://calmind-33a00.et.r.appspot.com/objects/"

    fun getApiService(): ApiService {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.HEADERS
        )

        val client = clientBuilder
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL.toHttpUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

}
