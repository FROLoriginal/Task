package com.example.tinkoffsoursecode.network

import com.example.tinkoffsoursecode.adapters.StrictTypeAdapter
import com.example.tinkoffsoursecode.model.PaymentResponse
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitDataProvider {

    private val BASE_URL = "http://82.202.204.94"

    fun getProvider(): RetrofitServices = Client().getInstance(BASE_URL).create(RetrofitServices::class.java)

    fun getStrictProvider(): RetrofitStrictServices {
        val gson = GsonBuilder()
            .registerTypeAdapter(PaymentResponse::class.java, StrictTypeAdapter())
            .create()

        return Client().getInstance(BASE_URL, GsonConverterFactory.create(gson))
            .create(RetrofitStrictServices::class.java)
    }

    companion object {
        fun getRequestBody(value: String): RequestBody {
            return RequestBody.create(MediaType.parse("multipart/form-data"), value)
        }
    }
}
