package com.example.fingerchat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.hyphenate.exceptions.HyphenateException

class RegisterPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        val options = EMOptions()
        options.setAppKey("1119221105109440#finger-chat")
        // 其他 EMOptions 配置。
        EMClient.getInstance().init(this, options)

        val tv2 = findViewById<TextView>(R.id.tv2)
        tv2.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }

        val btn2 = findViewById<Button>(R.id.register_btn)
        btn2.setOnClickListener{
            val mAccount = findViewById<EditText>(R.id.ev3)
            val mPassword = findViewById<EditText>(R.id.ev4)
            val re_mPassword = findViewById<EditText>(R.id.ev5)
            if(mPassword.text.toString().equals(re_mPassword.text.toString())) {
                Thread(
                    Runnable {
                        try {
                            EMClient.getInstance()
                                .createAccount(
                                    mAccount.text.toString(),
                                    mPassword.text.toString()
                                )//同步方法
                            //注册成功
                            Toast.makeText(
                                this,
                                "注册成功",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this, FirstPage::class.java))
                            this.finish()
                        }
                        catch (e: HyphenateException) {        //注册失败会抛出异常HyphenateException
                            runOnUiThread {
                                val errorCode = e.errorCode
                                when (errorCode) {
                                    EMError.NETWORK_ERROR -> Toast.makeText(
                                        this,
                                        "网络错误",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    EMError.USER_ALREADY_EXIST -> Toast.makeText(
                                        this,
                                        "用户已存在",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    EMError.USER_AUTHENTICATION_FAILED -> Toast.makeText(
                                        this,
                                        "注册失败，权限问题",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    EMError.USER_ILLEGAL_ARGUMENT -> Toast.makeText(
                                        this,
                                        "用户名称不合法",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    else -> Toast.makeText(
                                        this,
                                        "注册失败",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                ).start()
            }
            else{
                Toast.makeText(
                    this,
                    "密码前后不一致",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}