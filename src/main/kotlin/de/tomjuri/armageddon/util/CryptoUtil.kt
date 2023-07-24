package de.tomjuri.armageddon.util

import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

object CryptoUtil {
    fun rsaEncrypt(message: ByteArray, publicKey: RSAPublicKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedData = cipher.doFinal(message)
        return Base64.getEncoder().encodeToString(encryptedData)
    }

    fun rsaDecrypt(encryptedText: String, privateKey: RSAPrivateKey): String {
        val encryptedData = Base64.getDecoder().decode(encryptedText)
        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedData = cipher.doFinal(encryptedData)
        return String(decryptedData)
    }

    fun getPublicKey(key: String): RSAPublicKey {
        val keyBytes = Base64.getDecoder().decode(key)
        val spec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(spec) as RSAPublicKey
    }

    fun getPrivateKey(key: String): RSAPrivateKey {
        val keyBytes = Base64.getDecoder().decode(key)
        val spec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(spec) as RSAPrivateKey
    }
}