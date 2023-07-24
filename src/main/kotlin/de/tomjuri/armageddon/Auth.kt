package de.tomjuri.armageddon


import de.tomjuri.armageddon.util.CryptoUtil
import de.tomjuri.armageddon.util.mc
import okhttp3.OkHttpClient
import okhttp3.Request
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.MessageDigest
import java.security.Security
import java.util.*

object Auth {
    fun auth() {
        Security.addProvider(BouncyCastleProvider())
        val realDate = "${getHWID()}|${System.currentTimeMillis()}|${mc.session.playerID}".toByteArray()
        val publicKey = CryptoUtil.getPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtQF9Z1/UNv52OY+kpPOhRh4W5zBLZTD1o+zNGbYB9j7HZqWt09dcj+6fB2gYYHnBLHQpaJwFUT496r3pZLnYlMcQRFiw5u9VvGHc2UYfxnheE4AZF4FgzPIUnWM5hOCtEQ3DLkgtbjMdUNpV8vQr8I8cN15X+vOHv2egPH7d2ojPFLmmBywBcMo+2qar8iST3SWKJ+SY2DMDqWe739u4AWrCS7tqKxzaqDLrEOP6hHCrDH0bIWQ8EUvGoQlIU+/nj0l1iIS2zwBjeOvzpyo8HkF884HRgtf0l3evmAlNcy9g9hLeR4RP0sJXf8wMcQIZFWb54VAVh8Zhc9rv2ks6XwIDAQAB")
        val date = List(10) {
            println(it)
            if(it == 4) {
                CryptoUtil.rsaEncrypt(realDate, publicKey)
            } else {
                CryptoUtil.rsaEncrypt(generateRandomString(realDate.size).toByteArray(), publicKey)
            }
        }
        val bigString = date.joinToString("-")
        val url = "http://168.119.36.188:5000/Q6W2ecZlZ3InKEEriO7onlQ1i1vAVCC9ipOF4qwsfNIK1xg6Z80NZTgbXbOJ?DvigquT6UAztO5IdMb0iIex6skfcHrPs8lDeTCxLfzG98n0AdNWeIN2VX3Kd=$bigString"
        val request: Request = Request.Builder()
                .url(url)
                .addHeader("User-Agent", "TOM-CLIENT-JAVA8-Shipping-g1vJHlyarpLgba7Fu3SoUm5dGkoDHeI5f8QIFur7")
                .build()
        OkHttpClient().newCall(request).execute().use {
            val responseText: String = it.body()?.string() ?: return
            val privateKey = CryptoUtil.getPrivateKey("MIIEowIBAAKCAQEAvmWXw8r/wUhvlsy0h8NRKlDz8VzXY886PO776K/xkPDImbRikTvLOd5ltZGJb/gStYD5RcRlvYKkCcDbTnkrCrvki0jbUd1qOUtt7RCzzOyXkpsUfwyk4bbPBmvmzUFQZSng4PlnXa8Qpyt+YDVasSNjJ6M6T5VUwjRgTM6q5krpd/StZDJrJoHXm6Z/sPOG1GxL70hxENBoSzBj4rlZpfItk7U1Jf/RhNF0wYKdAnX+P+kruIazN98mjxwV3tmbZJ2Veow2yzna1fyP9Fd9Wrd1ZHIShVJ+6XH75Wh5dj0HyqrDBoVHxMcQrQDCKXau6FkB7h7oKRYY58SGO/y6iwIDAQABAoIBAB6qJ8nHJERB1tsaSB8viXR/ykeUNN/kjH7WMhNKtw9+1Z16dyo9+krCiMNirRiFWKS2oNnS5QdN3FPyeupwyx+08v0DNHOYbhchO2ikOS58PSNFv9orM/5jHi/CFCT4a9WHQP/q8JzJk9c2GD9a3dWhcJhTNT0uZ2gfMxKxHHGaKjwZdwR+ossWvBpJyGI1tVj17D7PqNhkxssuqR8XmU87NVS0t4Zn8/yCBtnFNRKkaXQqGQ7kFKwDINpwH92TOOMs193POW9+W3mSZUWDE+vI6a9LacKLY21S4HvawERcFBQ7pcVtwHAHxq3LQi5blNUwQmCIICUS8OcSJwl/X40CgYEAvui/OAaxyXoC24rtHFpl2y5SDcglvHxB0qXtFRbqIEUdorbZnKabwcOqg+0qu8QSd5W22htpq792OOiGA3zt9ekzm+MAFuTUsN6HuIJiKmd/rtWZXvFiVIDbMNqSQHfStu9sXkXnSTMlYvR5EHwVjkRYWkToMJAuhkiZyZrP9PUCgYEA/1Ag7t9R3R8wtbagBt0W6m/mxZU6iezWO6ZZ9Q9e0tSTYkKnGO3eyG8U9+BBIDKHd6mEJhppbR9pkPO/1UNIORh3U1Wbn+/oVwLc3V2BDNpF6omAEM4v/ljM+uzgxRygdpySN7pZ4rsqIca6XDqxvNCN1k9D84bl9ujnhxcgQX8CgYBwm5cXNBOwdRC0cRj1jXjhNUxfJrzjliO5XfgfZXnKeRpG/TYVCc18GK8+zClVwgzmjt/hfSPyk6fnX3iVHB6828Z+YAKQfn/aqBhERFwlCRGujf2+nAFVOQCRq4tzDXq0Pzuby/3mFo4p9WxnxdltJnE7JcQTySg4OyDDY1bg9QKBgAxoZ4FUZxUY+/QI0RQkUIXKNGvrybGEShESD5MiURhr7lG7LYW/obEZ/OnjAyc+bWj6WwrDA9aizS2XOv5xS6RCtSHeqjaGoUMBR5yWe7wVK4qvbgLjEmAJDsUvF+lcb8vz5gNBUjb7o5uKnen3jEXB2PLOXV4Lc0ehoAbnr9CfAoGBAIM97euJVUr5xLItaktahSLbJFB/ifmiVms8n6C51wBY9Th4LPojTbSKMfaiM5aaAn61QILdfVKqMagFCdl59VZsfMkOshWARbzrIaMpKrUe3YVLqDf8zSPmQSQfIfYXEOd9FlTZ8+3VMuBt4Gur2Hes+eyJEIHgSOGdlj9URmnL")
            val decoded = CryptoUtil.rsaDecrypt(responseText, privateKey)
            val split = decoded.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (System.currentTimeMillis() - split[2].toLong() < 10000) {
                if ("success" == split[0] && getHWID() == split[1]) {
                    println("Authentication successful!")
                } else {
                    println("Authentication failed!")
                    throw RuntimeException("Authentication failed!")
                }
            } else {
                println("Failed to authenticate!")
            }
        }
    }

    private fun getHWID(): String {
        val toEncrypt = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL")
        val md = MessageDigest.getInstance("MD5")
        md.update(toEncrypt.toByteArray())
        val hexString = StringBuffer()
        val byteData = md.digest()
        for (aByteData in byteData) {
            val hex = Integer.toHexString(0xff and aByteData.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }

    private fun generateRandomString(length: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = Random()
        val sb = StringBuilder(length)
        for (i in 0 until length) {
            sb.append(characters[random.nextInt(characters.length)])
        }
        return sb.toString()
    }

}