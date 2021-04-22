package com.example.tinkoffsoursecode.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tinkoffsoursecode.R
import com.example.tinkoffsoursecode.adapters.RecyclerViewAdapter
import com.example.tinkoffsoursecode.model.Payment


class PaymentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.payments_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run {
            val data = (get(DATA) as List<*>).throwExceptionIfNotMeet()
            view.findViewById<RecyclerView>(R.id.payments_recyclerView).run {
                setHasFixedSize(true)
                adapter = RecyclerViewAdapter(data.sortedBy { it.created })
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.HORIZONTAL
                    )
                )
            }
        }
    }

    private fun List<*>.throwExceptionIfNotMeet(): List<Payment> {
        val data = this.filterIsInstance<Payment>()
        if (data.size != this.size) throw IllegalArgumentException("Переданы неверные данные в Bundle (должен быть List<Payment>)")
        return data
    }

    companion object {
        fun newInstance(payments: List<Payment>): PaymentsFragment {
            return PaymentsFragment().also { it.arguments = bundleOf(Pair(DATA, payments)) }
        }

        const val DATA = "DATA_PAYMENTS"
        const val TAG = "ContentFragment"
    }
}
