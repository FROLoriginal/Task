package com.example.tinkoffsoursecode.presenters

import android.content.Context
import com.example.tinkoffsoursecode.AESUtil
import com.example.tinkoffsoursecode.interfaces.ISetPersonalPasswordUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonalPasswordPresenter(private val mInterface: ISetPersonalPasswordUI) {

    //Сохраняем зашифрованный токен в БД
    suspend fun setPassword(context: Context, token: String, first: String, second: String) {

        withContext(Dispatchers.IO) {
        if (first.meetTo(second)) {
            context.getSharedPreferences(AESUtil.TOKEN_TABLE, Context.MODE_PRIVATE)
                .edit()
                .putString(AESUtil.PASSWORD, AESUtil.encrypt(token, first))
                .apply()
            withContext(Dispatchers.Main) { mInterface.passwordSet() }
        } else withContext(Dispatchers.Main) { mInterface.tryAgain() }
    }
}
    private fun String.meetTo(second: String): Boolean {
        return this == second && this.isNotEmpty() && second.isNotEmpty()
    }
}
