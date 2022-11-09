package com.example.fingerchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hyphenate.EMCallBack
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.hyphenate.exceptions.HyphenateException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val options = EMOptions()
        options.setAppKey("1119221105109440#finger-chat")
        // 其他 EMOptions 配置。
        EMClient.getInstance().init(this, options)

        val connectionListener: EMConnectionListener = object : EMConnectionListener {
            override fun onConnected() {
                if(EMClient.getInstance().isLoggedIn){
                    startActivity(Intent(this@MainActivity, FirstPage::class.java))
                    this@MainActivity.finish()
                }
            }
            override fun onDisconnected(errorCode: Int) {}
            override fun onLogout(errorCode: Int) {}
            override fun onTokenWillExpire() {}
            override fun onTokenExpired() {}
        }
// 注册连接状态监听
        EMClient.getInstance().addConnectionListener(connectionListener)
// 解注册连接状态监听
//        EMClient.getInstance().removeConnectionListener(connectionListener)


        val tv1 = findViewById<TextView>(R.id.tv1)
        tv1.setOnClickListener{
            startActivity(Intent(this, RegisterPage::class.java))
            this.finish()
        }

        val btn1 = findViewById<Button>(R.id.login_btn)
        btn1.setOnClickListener{
            val mAccount = findViewById<EditText>(R.id.ev1)
            val mPassword = findViewById<EditText>(R.id.ev2)
            EMClient.getInstance().login(mAccount.text.toString(), mPassword.text.toString(), object : EMCallBack {
                // 登录成功回调
                override fun onSuccess() {
                    startActivity(Intent(this@MainActivity, FirstPage::class.java))
                    this@MainActivity.finish()
                }

                // 登录失败回调，包含错误信息
                override fun onError(code: Int, error: String) {
                    Log.e("login-error", error)
                }

                override fun onProgress(i: Int, s: String) {}
            })
        }

    }

//    fun repalceFragment(fragment: Fragment){
//        var fragmentManager = supportFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//        var oldfragment = fragmentManager.findFragmentById(R.id.fL)
//        if(oldfragment ==null || oldfragment != fragment){
//            transaction.add(R.id.fL,fragment)
//        }
//        var ls = fragmentManager.fragments
//        if(ls != null){
//            ls.forEach{
//                transaction.hide(it)
//            }
//        }
//        transaction.show(fragment)
//        transaction.commit()
//    }
}