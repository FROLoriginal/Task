package com.example.tinkoffsoursecode.presenters

import android.content.Context
import com.example.tinkoffsoursecode.AESUtil
import com.example.tinkoffsoursecode.interfaces.IAuthViaPasswordUI
import com.example.tinkoffsoursecode.model.Error
import com.example.tinkoffsoursecode.model.PaymentResponse
import com.example.tinkoffsoursecode.network.RetrofitDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViaPasswordPresenter(private val authViaPasswordUI: IAuthViaPasswordUI) {

    suspend fun getPayments(context: Context, password: String) {

        withContext(Dispatchers.IO) {
            val token = AESUtil.decrypt(getToken(context), password)
            if (token == null) withContext(Dispatchers.Main) {authViaPasswordUI.onError(Error.Reason.INVALID_TOKEN)}
            else {
                val response = RetrofitDataProvider()
                    .getStrictProvider()
                    .getPayments(token)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        when (val body: PaymentResponse? = response.body()) {
                            null -> authViaPasswordUI.onError(Error.Reason.INCORRECT_SERVER_RESPONSE)
                            else -> {
                                if (body.success) successPayments(body)
                                else errorPayments(body)
                            }
                        }
                    } else authViaPasswordUI.onError(Error.Reason.INTERNET_CONNECTION)
                }
            }
        }
    }
    private fun successPayments(body: PaymentResponse) {
        body.response.let {
            if (it == null) authViaPasswordUI.onError(Error.Reason.INCORRECT_SERVER_RESPONSE)
            else authViaPasswordUI.onSuccess(it)
        }
    }

    private fun errorPayments(body: PaymentResponse) {
        body.error.let { error ->
            when (error) {
                null -> authViaPasswordUI.onError(Error.Reason.INCORRECT_SERVER_RESPONSE)
                else -> authViaPasswordUI.onError(
                    Error.Reason.values().find { error.error_code == it.error_code }!!
                )
            }
        }
    }

    private fun getToken(context: Context): String {
        return context
            .getSharedPreferences(AESUtil.TOKEN_TABLE, Context.MODE_PRIVATE)
            .getString(AESUtil.PASSWORD, "")!!
    }
}
