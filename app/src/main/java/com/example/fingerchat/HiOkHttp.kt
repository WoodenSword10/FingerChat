package com.example.fingerchat

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast

import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.File

// object关键字不需要实例化，直接使用HiOkHttp.init()即可
object HiOkHttp {
    val client = OkHttpClient.Builder()     // builder构造者设计模式
        .connectTimeout(10, TimeUnit.SECONDS)   // 连接超时时间
        .readTimeout(10, TimeUnit.SECONDS)   // 读取超时
        .writeTimeout(10, TimeUnit.SECONDS)  // 写超时
        .addInterceptor(LoggingInterceptor()) // 调用拦截器
        .build();

    // android分为主线程和子线程
    // 主线程就是APP一启动后，android framework层会启动一个线程，即主线程（UI线程）
    // 子线程 ———— new Thread().start()

    // 同步GET会一直等待http请求，直到返回了响应，在这之间会阻塞线程，
    // 所以同步请求不能再Android的主线程中执行，否则会报错NetworkMainThreadException
    fun get(url: String) {
        // 线程执行
        Thread(Runnable {
            // 构造请求体
            val request: Request = Request.Builder()
                .url(url)
                .build()

            // 构造请求对象
            val call: Call = client.newCall(request)
            // 发起同步请求execute--
            val response = call.execute()

            val body = response.body?.string()
            println("get response:${body}")
        }).start()
    }

    // 异步请求
    fun getAsync(url: String) {
        // 构造请求体
        val request: Request = Request.Builder()
            .url(url)
            .build()

        // 构造请求对象
        val call: Call = client.newCall(request)
        // 发起异步请求enqueue，没有返回值，结果通过回调Callback的onResponse方法及onFailure方法处理
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GetFail", "get response onFailure:${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println("get response:${body}")
            }
        })
    }

    // 同步POST
    fun post(url: String) {
        val body = FormBody.Builder()
            .add("name", "ljw")
            .add("password", "120579")
            .build()
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        val call = client.newCall((request))
        Thread(Runnable {
            val response = call.execute()
            Log.e("post", "post response:${response.body?.string()}")
        }).start()
    }

    // 异步POST
    fun postAsync(url: String) {
        val body = FormBody.Builder()
            .add("name", "ljw")
            .add("password", "120579")
            .build()
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        val call = client.newCall((request))
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GetFail", "postAsync response onFailure:${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                Log.e("post", "postAsync response:${body}")
            }
        })
    }

    // post异步请求，用于多表单文件上传，注意接口必须支持文件上传操作
    fun postAsyncMultipart(url: String, context: Context) {
        // 读取文件,需要动态申请权限
        val file = File(Environment.getExternalStorageDirectory(), "1.png")
        if(!file.exists()){
            Toast.makeText(context, "cs", Toast.LENGTH_SHORT).show()
            return
        }
        val body = MultipartBody.Builder()
            .addFormDataPart("key", "value1")
            .addFormDataPart("key2", "value2")
            .addFormDataPart("file", "file.txt", RequestBody.create("application/octet-stream".toMediaType(),file))
            .build()
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        val call = client.newCall(request)
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GetFail", "postAsyncMultipart response onFailure:${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                Log.e("post", "postAsyncMultipart response:${body}")
            }
        })
    }

    // 异步提交字符串
    fun postString(url: String, string: String){
        // json格式
        val jsonobj = JSONObject()
        jsonobj.put("key1", "value1")
        jsonobj.put("key2", "value2")

        // 纯文本
//        val textPaint = "text/plain;charset=utf-8".toMediaType()
//        val textobj = "username:username;password:password"
//        val body = RequestBody.create(textPaint,textobj)

        val body = RequestBody.create("application/json;charset=utf-8".toMediaType(),jsonobj.toString())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val call = client.newCall(request)
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GetFail", "postString response onFailure:${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                Log.e("post", "postString response:${body}")
            }
        })
    }


}