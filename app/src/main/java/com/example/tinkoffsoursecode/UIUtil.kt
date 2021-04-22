package com.example.tinkoffsoursecode

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
fun Fragment.toast(message: String) {
    requireContext().toast(message)
}
