package com.example.tinkoffsoursecode.interfaces

import com.example.tinkoffsoursecode.model.Error
import com.example.tinkoffsoursecode.model.Payment

interface IAuthViaPasswordUI {
    // Интерфейс описывающий ошибку/успех на авторизацию через персональный ключ
    fun onError(reason: Error.Reason)
    fun onSuccess(data: List<Payment>)
}
