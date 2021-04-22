package com.example.tinkoffsoursecode.interfaces

import com.example.tinkoffsoursecode.model.Error

interface IAuthViaSecretsUI {
    // Интерфейс описывающий ошибку/успех на авторизацию через логин и пароль
    fun onErrorAuth(reason: Error.Reason)
    fun onSuccessAuth(token: String)

}
