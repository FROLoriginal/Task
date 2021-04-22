package com.example.tinkoffsoursecode.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.tinkoffsoursecode.R
import com.example.tinkoffsoursecode.interfaces.ISetPersonalPasswordUI
import com.example.tinkoffsoursecode.presenters.PersonalPasswordPresenter
import com.example.tinkoffsoursecode.toast
import kotlinx.coroutines.*

class CreatePersonalPasswordFragment : Fragment(),
    ISetPersonalPasswordUI {

    private val setPassword by lazy(LazyThreadSafetyMode.NONE) {
        requireView().findViewById<Button>(
            R.id.save_password_button
        )
    }
    private val firstField by lazy(LazyThreadSafetyMode.NONE) {
        requireView().findViewById<EditText>(
            R.id.app_password_input
        )
    }
    private val secondField by lazy(LazyThreadSafetyMode.NONE) {
        requireView().findViewById<EditText>(
            R.id.app_confirm_password_input
        )
    }
    private val presenter = PersonalPasswordPresenter(this)
    private lateinit var moving: Moving
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_password_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            arguments?.run {
                val token = getString(TOKEN)!!
                setPassword.setOnClickListener {
                    job = GlobalScope.launch(Dispatchers.Main) {
                        presenter.setPassword(
                            requireContext(),
                            token,
                            firstField.text.toString(),
                            secondField.text.toString()
                        )
                    }
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

    //Вызывается когда персональный пароль был установлен
    override fun passwordSet() {
        moving.onPasswordSet()
    }
    //Вызывается в случае если два пароля не одинаковые или < 4 символов
    override fun tryAgain() {
        firstField.text.clear()
        secondField.text.clear()
        toast(resources.getString(R.string.password_must_meet_requirements))
    }

    interface Moving {
        fun onPasswordSet()
    }

    companion object {

        fun newInstance(token: String) = CreatePersonalPasswordFragment()
            .also { it.arguments = bundleOf(Pair(TOKEN, token)) }

    const val TAG = "CreatePersonalPasswordFragment"
    const val TOKEN = "TOKEN"
}
}
