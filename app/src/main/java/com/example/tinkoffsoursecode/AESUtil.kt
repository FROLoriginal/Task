package com.example.tinkoffsoursecode

import android.util.Base64
import java.lang.reflect.InvocationTargetException
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object AESUtil {

    //возвращаем зашифрованную строку
    fun encrypt(target: String, password: String): String {
        val stronger = password + password + password + password
        val secretKeySpec = SecretKeySpec(stronger.toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = stronger.toCharArray()
        for (i in charArray.indices) {
            iv[i] = charArray[i].toByte()
        }
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

        } catch (e: InvocationTargetException) {
            e.cause
        }

        val encryptedValue = cipher.doFinal(target.toByteArray())
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }

    //Вовзвращаем расшифрованную строку
    fun decrypt(target: String, password: String): String? {
        val stronger = password + password + password + password

        val secretKeySpec = SecretKeySpec(stronger.toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = stronger.toCharArray()
        for (i in charArray.indices) {
            iv[i] = charArray[i].toByte()
        }
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        val decryptedByteValue: ByteArray
        try {
            decryptedByteValue = cipher.doFinal(Base64.decode(target, Base64.DEFAULT))
        } catch (e: AEADBadTagException) {
            return null
        }
        return String(decryptedByteValue)
    }

    const val TOKEN_TABLE = "TOKEN_TABLE"
    const val PASSWORD = "PASSWORD"
}
