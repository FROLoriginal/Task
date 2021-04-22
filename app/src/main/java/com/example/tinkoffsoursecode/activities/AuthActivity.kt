package com.example.tinkoffsoursecode.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tinkoffsoursecode.AESUtil
import com.example.tinkoffsoursecode.R
import com.example.tinkoffsoursecode.fragments.AuthViaPersonalPasswordFragment
import com.example.tinkoffsoursecode.fragments.AuthViaSecretsFragment
import com.example.tinkoffsoursecode.fragments.CreatePersonalPasswordFragment
import com.example.tinkoffsoursecode.fragments.PaymentsFragment
import com.example.tinkoffsoursecode.model.Payment

class AuthActivity : AppCompatActivity(),
    AuthViaSecretsFragment.Moving,
    AuthViaPersonalPasswordFragment.Moving,
    CreatePersonalPasswordFragment.Moving {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        title = getString(R.string.authorization)
        if (isPasswordExists())
            goTo(AuthViaPersonalPasswordFragment(), AuthViaPersonalPasswordFragment.TAG)
        else goTo(AuthViaSecretsFragment(), AuthViaSecretsFragment.TAG)

    }
    //Срабатывает когда был установлен персональный пароль
    override fun onPasswordSet() {
        title = getString(R.string.input_personal_pass_title)
        goTo(AuthViaPersonalPasswordFragment(), AuthViaSecretsFragment.TAG)
    }
    //Сработает когда нужно показывать транзакции
    override fun onShowPayments(data: List<Payment>) {
        title = getString(R.string.recyclerView_title)
        goTo(PaymentsFragment.newInstance(data), PaymentsFragment.TAG)
    }
    //Сработает когда мы авторизируемся
    override fun onAuthorized(token: String) {
        title = getString(R.string.create_personal_pass_title)
        goTo(CreatePersonalPasswordFragment.newInstance(token), CreatePersonalPasswordFragment.TAG)
    }
    //Функция перехода по фрагментам
    private fun goTo(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.container_nav, fragment, tag)
            commit()
        }
    }

    private fun isPasswordExists() =
        getSharedPreferences(AESUtil.TOKEN_TABLE, Context.MODE_PRIVATE).contains(AESUtil.PASSWORD)

}
