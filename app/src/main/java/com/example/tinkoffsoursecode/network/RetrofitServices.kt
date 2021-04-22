package com.example.tinkoffsoursecode.network

import com.example.tinkoffsoursecode.model.AuthResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RetrofitServices {

    @Multipart
    @Headers("app-key: 12345", "v: 1")
    @POST("api-test/login")
   suspend fun authorization(
        @Part("login") login: RequestBody,
        @Part("password") password: RequestBody
    ): Response<AuthResponse>
}
