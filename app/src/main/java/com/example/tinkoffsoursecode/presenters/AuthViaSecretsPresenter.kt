package com.example.tinkoffsoursecode.presenters

import com.example.tinkoffsoursecode.interfaces.IAuthViaSecretsUI
import com.example.tinkoffsoursecode.model.AuthResponse
import com.example.tinkoffsoursecode.model.Error
import com.example.tinkoffsoursecode.network.RetrofitDataProvider
import com.example.tinkoffsoursecode.network.RetrofitServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AuthViaSecretsPresenter(val mainUi: IAuthViaSecretsUI) {

    private lateinit var token: String

    private val services: RetrofitServices by lazy { RetrofitDataProvider().getProvider() }

    suspend fun authorization(login: String, password: String) {

        withContext(Dispatchers.IO) {
            val requestLogin = RetrofitDataProvider.getRequestBody(login)
            val requestPassword = RetrofitDataProvider.getRequestBody(password)

            val response = services.authorization(requestLogin, requestPassword)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    when (val body: AuthResponse? = response.body()) {
                        null -> mainUi.onErrorAuth(Error.Reason.INCORRECT_SERVER_RESPONSE)
                        else -> {
                            if (body.success.toBoolean()) successAuth(body)
                            else errorAuth(body)
                        }
                    }

                } else mainUi.onErrorAuth(Error.Reason.INTERNET_CONNECTION)
            }
        }
    }
    private fun successAuth(body: AuthResponse) {
        body.response.let {
            if (it == null) mainUi.onErrorAuth(Error.Reason.INCORRECT_SERVER_RESPONSE)
            else {
                token = it.token
                mainUi.onSuccessAuth(token)
            }
        }
    }

    private fun errorAuth(body: AuthResponse) {
        body.error.let { error ->
            when (error) {
                null -> mainUi.onErrorAuth(Error.Reason.INCORRECT_SERVER_RESPONSE)
                else -> mainUi.onErrorAuth(Error.Reason.values().find { it.error_code == error.error_code }!!)
            }
        }
    }
}
