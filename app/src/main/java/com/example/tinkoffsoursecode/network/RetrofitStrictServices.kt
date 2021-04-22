package com.example.tinkoffsoursecode.network

import com.example.tinkoffsoursecode.model.PaymentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitStrictServices {

    @Headers("app-key: 12345", "v: 1")
    @GET("api-test/payments")
   suspend fun getPayments(@Query("token") token: String): Response<PaymentResponse>
}
