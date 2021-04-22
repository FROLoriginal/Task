package com.example.tinkoffsoursecode.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {

    private var retrofit: Retrofit? = null

    fun getInstance(
        baseUrl: String,
        factory: GsonConverterFactory = GsonConverterFactory.create()
    ): Retrofit {

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .build()
        }

        return retrofit!!

    }


}
