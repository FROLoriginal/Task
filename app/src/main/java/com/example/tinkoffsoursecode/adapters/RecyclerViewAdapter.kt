package com.example.tinkoffsoursecode.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tinkoffsoursecode.R
import com.example.tinkoffsoursecode.model.Payment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewAdapter(
    private val payments: List<Payment>
) :
    RecyclerView.Adapter<PaymentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.payment_card_view, parent, false)
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = payments[position]
        holder.desc.text = payment.desc
        holder.currencyPlusAmount.text = holder.itemView.resources.getString(
            R.string.currency_plus_amount_cardview,
            payment.currency,
            payment.amount.convertAmount())
        holder.created.text = payment.created.convertToDateTime()
    }

    override fun getItemCount() = payments.size

    private fun Long.convertToDateTime(): String {
        val dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT)
        //Умножаем на 1000 т.к мы содержим значения в секундах, на вход подаются милисекунду
        return dateFormat.format(Date(this * 1000L))
    }

    private fun Double.convertAmount() = toBigDecimal().toPlainString()
}
