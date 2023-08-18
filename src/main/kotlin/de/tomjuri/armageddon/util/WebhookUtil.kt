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

    fun update(url: String, messageId: String, content: String) : Response {
        val request = Request.Builder()
            .url("$url/messages/$messageId")
            .patch(RequestBody.create(MediaType.parse("application/json"), content))
            .build()
        val response = OkHttpClient().newCall(request).execute()
        response.close()
        return response
    }

    fun delete(url: String, messageId: String) : Response {
        val request = Request.Builder()
            .url("$url/messages/$messageId")
            .delete()
            .build()
        val response = OkHttpClient().newCall(request).execute()
        response.close()
        return response
    }

    fun uploadFile(url: String, file: ByteArray, fileName: String) : Response {
       val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("application/octet-stream"), file))
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        val response = OkHttpClient().newCall(request).execute()
        response.close()
        return response
    }
}