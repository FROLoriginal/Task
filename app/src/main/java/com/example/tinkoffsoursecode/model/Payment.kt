package com.example.tinkoffsoursecode.model

data class Payment(
    val desc: String,
    val amount: Double,
    val currency: String,
    val created: Long
)
