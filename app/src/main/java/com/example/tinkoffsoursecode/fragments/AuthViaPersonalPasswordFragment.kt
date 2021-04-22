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
import com.example.tinkoffsoursecode.interfaces.IAuthViaPasswordUI
import com.example.tinkoffsoursecode.model.Error
import com.example.tinkoffsoursecode.model.Payment
import com.example.tinkoffsoursecode.presenters.AuthViaPasswordPresenter
import kotlinx.coroutines.*

class AuthViaPersonalPasswordFragment : Fragment(), IAuthViaPasswordUI {

    private val button by lazy { requireView().findViewById<Button>(R.id.auth_by_personal_password) }
    private val editText by lazy { requireView().findViewById<EditText>(R.id.personal_password_et) }
    private val presenter = AuthViaPasswordPresenter(this)
    private lateinit var moving: Moving
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.input_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.run {  }

        button.setOnClickListener {
            button.isClickable = false
            job = GlobalScope.launch(Dispatchers.Main) {
                presenter.getPayments(requireContext(), editText.text.toString())
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        moving = context as Moving
    }

    override fun onDetach() {
        super.onDetach()
        job?.cancelChildren()
    }
    //Вызывается если пароль указан неверно
    override fun onError(reason: Error.Reason) {
        button.isClickable = true
        editText.text.clear()
    }
    //Вызывается если пароль указан верно
    override fun onSuccess(data: List<Payment>) {
        moving.onShowPayments(data)
    }

    interface Moving {
        fun onShowPayments(data: List<Payment>)
    }

    companion object {
        const val TAG = "AuthByPasswordFragment"
    }
}
