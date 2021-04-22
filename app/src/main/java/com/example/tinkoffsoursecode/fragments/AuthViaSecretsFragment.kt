package com.example.tinkoffsoursecode.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.tinkoffsoursecode.R
import com.example.tinkoffsoursecode.model.Error
import com.example.tinkoffsoursecode.interfaces.IAuthViaSecretsUI
import com.example.tinkoffsoursecode.presenters.AuthViaSecretsPresenter
import com.example.tinkoffsoursecode.toast
import kotlinx.coroutines.*

class AuthViaSecretsFragment : Fragment(), IAuthViaSecretsUI {

    private val presenter = AuthViaSecretsPresenter(this)
    private val passwordEt by lazy(LazyThreadSafetyMode.NONE) {
        requireView().findViewById<EditText>(
            R.id.auth_password
        )
    }
    private val loginEt by lazy(LazyThreadSafetyMode.NONE) { requireView().findViewById<EditText>(R.id.auth_login) }
    private val button by lazy(LazyThreadSafetyMode.NONE) { requireView().findViewById<Button>(R.id.authButton) }
    private lateinit var moving: Moving
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            if (isDataInput()) {
                button.isClickable = false
                job = GlobalScope.launch(Dispatchers.IO) {
                    presenter.authorization(loginEt.text.toString(), passwordEt.text.toString())
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        moving = context as Moving
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        job?.cancelChildren()
    }
    //Вызывается когда авторизация не удалась
    override fun onErrorAuth(reason: Error.Reason) {
        button.isClickable = true
        when (reason) {
            Error.Reason.INVALID_APP_KEY -> toast(resources.getString(R.string.bad_app_key))
            Error.Reason.INTERNET_CONNECTION -> toast(resources.getString(R.string.bad_connection))
            Error.Reason.INCORRECT_SERVER_RESPONSE -> toast(resources.getString(R.string.bad_server_response))
            Error.Reason.INCORRECT_AUTH_DATA -> {
                loginEt.text.clear()
                passwordEt.text.clear()
                toast(resources.getString(R.string.login_or_password_not_correct))
            }
            else -> toast(resources.getString(R.string.unknown_error))
        }
    }
    //Вызывается когда авторизация удалась и токен был получен
    override fun onSuccessAuth(token: String) {
        moving.onAuthorized(token)
    }
    //Проверка на то оба ли поля заполнены
    private fun isDataInput() = loginEt.text.isNotEmpty() && passwordEt.text.isNotEmpty()

    interface Moving {
        fun onAuthorized(token: String)
    }

    companion object {
        const val TAG = "AuthFragment"
    }

}
