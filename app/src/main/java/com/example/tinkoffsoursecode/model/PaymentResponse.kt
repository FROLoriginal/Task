package com.example.tinkoffsoursecode.model

data class PaymentResponse (
    val success: Boolean,
    val error: Error?,
    val response: List<Payment>?
)
