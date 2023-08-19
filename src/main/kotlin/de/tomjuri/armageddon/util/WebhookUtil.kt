package de.tomjuri.armageddon.util

import okhttp3.*

object WebhookUtil {
    fun send(url: String, content: String) : Response {
        val request = Request.Builder()
            .url(url)
            .post(RequestBody.create(MediaType.parse("application/json"), content))
            .build()
        val response = OkHttpClient().newCall(request).execute()
        response.close()
        return response
    }
}