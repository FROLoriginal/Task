package com.example.tinkoffsoursecode.model

data class Error(
    val error_code: Int,
    val error_msg: String
) {
    enum class Reason(val error_code: Int?) {
        INVALID_APP_KEY(100),
        INVALID_TOKEN(104),
        INTERNET_CONNECTION(null),
        INCORRECT_AUTH_DATA(102),
        INCORRECT_SERVER_RESPONSE(400)
    }
}

