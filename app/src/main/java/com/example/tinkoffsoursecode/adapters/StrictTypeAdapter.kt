package com.example.tinkoffsoursecode.adapters

import com.example.tinkoffsoursecode.model.Error
import com.example.tinkoffsoursecode.model.Payment
import com.example.tinkoffsoursecode.model.PaymentResponse
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.IllegalStateException
import java.util.*

class StrictTypeAdapter : TypeAdapter<PaymentResponse>() {

    override fun write(out: JsonWriter?, value: PaymentResponse?) {
        throw UnsupportedOperationException()
    }

    override fun read(input: JsonReader): PaymentResponse {

        var payments: LinkedList<Payment>? = null
        var error: Error? = null
        input.beginObject()                                     //{
        input.nextName()                                        //"success":
        val success = input.nextString()!!.toBoolean()          //"true",
        if (input.nextName() == "response") {                   //"response":
            payments = LinkedList()
            input.beginArray()                                  //[
            while (input.peek() != JsonToken.END_ARRAY) {
                try {
                    input.beginObject()                         //{
                    input.nextName()                            //"desc":
                    if (input.skipIfNextFieldIsNot(JsonToken.STRING)) continue
                    val desc = input.nextString()               //"sometext",
                    if (desc.skipIfEmpty(input)) continue
                    input.nextName()                            //"amount":
                    if (input.skipIfNextFieldIsNot(JsonToken.NUMBER)) continue
                    val amount = input.nextDouble()             //10.0,
                    input.nextName()                            //"currency":
                    if (input.skipIfNextFieldIsNot(JsonToken.STRING)) continue
                    val currency = input.nextString()           //"BTC",
                    if (currency.skipIfEmpty(input)) continue
                    input.nextName()                            //"created":
                    if (input.skipIfNextFieldIsNot(JsonToken.NUMBER)) continue
                    val created: Long = input.nextLong()        //150000000
                    input.endObject()                           //}
                    payments.add(Payment(desc, amount, currency, created))
                } catch (e: IllegalStateException) {
                    input.endObject()
                }
            }
            input.endArray()                                    //]
            input.endObject()                                   //}
        } else {
            input.nextName()                                    //"error":
            input.beginObject()                                 //{
            input.nextName()                                    //"error_code":
            val error_code = input.nextInt()                    //100,
            input.nextName()                                    //"error_msg":
            val error_msg = input.nextString()                  //"some msg"
            input.endObject()                                   //}
            input.endObject()                                   //}
            error = Error(error_code, error_msg)
        }
        return PaymentResponse(success, error, payments)

    }

    //Проверка на то соответствует ли поле желаемому
    private fun JsonReader.skipIfNextFieldIsNot(token: JsonToken): Boolean {
        if (this.peek() != token) {
            skip(this)
            return true
        }
        return false
    }
    //Если строка пуста то пропускаем все объекты
    private fun String.skipIfEmpty(input: JsonReader): Boolean {
        if (this.isEmpty()) {
            if (input.peek() != JsonToken.END_OBJECT) input.nextName()
            skip(input)
            return true
        }
        return false
    }
    //Пропускаем все объекты
    private fun skip(input: JsonReader) {
        while (input.peek() != JsonToken.BEGIN_OBJECT) {
            input.skipValue()
            input.nextName()
        }
    }
}
