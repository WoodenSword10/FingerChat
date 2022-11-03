package com.example.fingerchat

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer

class LoggingInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 获取当前时间戳
        val time_start = System.nanoTime()
        // 获取请求
        val request = chain.request()
        // 获取响应
        val response = chain.proceed(request)
        val buffer = Buffer()
        request.body?.writeTo(buffer)
        var resquestBodyStr = buffer.readUtf8()
        Log.e("OKHTTP",String.format("Sending request %s with params %s", request.url, resquestBodyStr))

        // response.body?.string()只能调用一次，其他地方再调用会报错
        val bussinessData = response.body?.string()?:"response body null"
        // 重构response.body?.string()
        val mediaType = response.body?.contentType()
        val newBody = ResponseBody.create(mediaType, bussinessData)
        val newResponse = response.newBuilder().body(newBody).build()

        val time_end = System.nanoTime()
        Log.e("OKHTTP",String.format("Received response for %s in %.1fms >>> %s", request.url, (time_end-time_start)/1e6, bussinessData))
        return newResponse
    }
}