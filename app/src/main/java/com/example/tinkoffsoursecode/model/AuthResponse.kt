package com.example.tinkoffsoursecode.model

data class AuthResponse(
    val success: String,
    val response: SuccessAuth?,
    val error: Error?

)
