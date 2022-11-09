package com.example.fingerchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.hyphenate.chat.EMUserInfo
import com.hyphenate.chat.EMUserInfo.EMUserInfoType


class InfoChangePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_change_page)

        val options = EMOptions()
        options.setAppKey("1119221105109440#finger-chat")
        // 其他 EMOptions 配置。
        EMClient.getInstance().init(this, options)

        var name_ev = findViewById<TextView>(R.id.name_ev)
        var birth_ev = findViewById<TextView>(R.id.birth_ev)
        var mail_ev = findViewById<TextView>(R.id.mail_ev)
        var gender_ev = findViewById<TextView>(R.id.gender_ev)
        var phone_ev = findViewById<TextView>(R.id.phone_ev)
        var sign_ev = findViewById<TextView>(R.id.sign_ev)

        val userId = arrayOfNulls<String>(1)
        userId[0] = EMClient.getInstance().currentUser
        val userInfoTypes = arrayOfNulls<EMUserInfoType>(6)
        userInfoTypes[0] = EMUserInfoType.NICKNAME
        userInfoTypes[1] = EMUserInfoType.PHONE
        userInfoTypes[2] = EMUserInfoType.BIRTH
        userInfoTypes[3] = EMUserInfoType.EMAIL
        userInfoTypes[4] = EMUserInfoType.GENDER
        userInfoTypes[5] = EMUserInfoType.SIGN
        EMClient.getInstance().userInfoManager().fetchUserInfoByAttribute(userId, userInfoTypes,
            object : EMValueCallBack<Map<String?, EMUserInfo?>?> {
                override fun onError(error: Int, errorMsg: String?) {
                    Log.e("user-info-error", errorMsg.toString())
                }

                override fun onSuccess(value: Map<String?, EMUserInfo?>?) {
                    val userInfo: EMUserInfo = value?.get(EMClient.getInstance().currentUser)!!
                    //昵称
                    if (userInfo != null && userInfo.nickname != null && userInfo.nickname.length > 0) {
                        name_ev.setText(userInfo.nickname)
                        birth_ev.setText(userInfo.birth)
                        mail_ev.setText(userInfo.email)
                        if(userInfo.gender == 1){
                            gender_ev.setText("男")
                        }else{
                            gender_ev.setText("女")
                        }
                        phone_ev.setText(userInfo.phoneNumber)
                        sign_ev.setText(userInfo.signature)
                        EMClient.getInstance().updateCurrentUserNick(userInfo.nickname)
                    }
                }
            }
        )

        val back_info_btn = findViewById<Button>(R.id.back_info_btn)
        back_info_btn.setOnClickListener{
            startActivity(Intent(this, InfoPage::class.java))
            this.finish()
        }

        val commit_btn = findViewById<Button>(R.id.commit_btn)
        commit_btn.setOnClickListener{
            // 设置所有用户属性。
            // 设置所有用户属性。
            val userInfo = EMUserInfo()
            userInfo.userId = EMClient.getInstance().currentUser
            userInfo.nickname = name_ev.text.toString()
            userInfo.birth = birth_ev.text.toString()
            userInfo.signature = sign_ev.text.toString()
            userInfo.phoneNumber = phone_ev.text.toString()
            userInfo.email = mail_ev.text.toString()
            if(gender_ev.text.toString().equals("男")){
                userInfo.gender = 1
            }else{
                userInfo.gender = 0
            }
            EMClient.getInstance().userInfoManager()
                .updateOwnInfo(userInfo, object : EMValueCallBack<String?> {
                    override fun onSuccess(value: String?) {
                        startActivity(Intent(this@InfoChangePage, InfoPage::class.java))
                        this@InfoChangePage.finish()
                    }
                    override fun onError(error: Int, errorMsg: String) {
                        Toast.makeText(this@InfoChangePage, "更新失败", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}