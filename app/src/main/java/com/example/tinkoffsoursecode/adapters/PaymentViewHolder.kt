package com.example.tinkoffsoursecode.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tinkoffsoursecode.R

class PaymentViewHolder(view: View): RecyclerView.ViewHolder(view) {

    //Оптимизация позволяющая не вызывать поля через get/set
    @JvmField
    val desc: TextView = view.findViewById(R.id.description)
    @JvmField
    val currencyPlusAmount: TextView = view.findViewById(R.id.currencyPlusAmount)
    @JvmField
    val created: TextView = view.findViewById(R.id.created)
}
