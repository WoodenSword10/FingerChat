package com.example.fingerchat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions


class FirstPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        val options = EMOptions()
        options.setAppKey("1119221105109440#finger-chat")
        // 其他 EMOptions 配置。
        EMClient.getInstance().init(this, options)

        EMClient.getInstance().chatManager().loadAllConversations()
        EMClient.getInstance().groupManager().loadAllGroups()

        val btn3 = findViewById<Button>(R.id.btn3)
        btn3.setOnClickListener{
            EMClient.getInstance().logout(true, object : EMCallBack {
                override fun onSuccess() {
                    startActivity(Intent(this@FirstPage, MainActivity::class.java))
                    this@FirstPage.finish()
                }
                override fun onError(code: Int, message: String) {
                    Toast.makeText(this@FirstPage,"退出失败"+message,Toast.LENGTH_SHORT).show()
                }
            })
        }

        val btn4 = findViewById<Button>(R.id.info_btn)
        btn4.setOnClickListener{
            startActivity(Intent(this, InfoPage::class.java))
            this.finish()
        }
    }
}