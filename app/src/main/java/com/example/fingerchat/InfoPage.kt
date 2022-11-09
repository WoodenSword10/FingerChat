package com.example.fingerchat

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.hyphenate.chat.EMUserInfo
import com.hyphenate.chat.EMUserInfo.EMUserInfoType


class InfoPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_page)

        val options = EMOptions()
        options.setAppKey("1119221105109440#finger-chat")
        // 其他 EMOptions 配置。
        EMClient.getInstance().init(this, options)

        var name_tv = findViewById<TextView>(R.id.name_tv)
        var birth_tv = findViewById<TextView>(R.id.birth_tv)
        var mail_tv = findViewById<TextView>(R.id.mail_tv)
        var gender_tv = findViewById<TextView>(R.id.gender_tv)
        var phone_tv = findViewById<TextView>(R.id.phone_tv)
        var sign_tv = findViewById<TextView>(R.id.sign_tv)
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
                        name_tv.setText(userInfo.nickname)
                        birth_tv.setText(userInfo.birth)
                        mail_tv.setText(userInfo.email)
                        if(userInfo.gender == 1){
                            gender_tv.setText("男")
                        }else{
                            gender_tv.setText("女")
                        }
                        phone_tv.setText(userInfo.phoneNumber)
                        sign_tv.setText(userInfo.signature)
                        EMClient.getInstance().updateCurrentUserNick(userInfo.nickname)
                    }
                }
            }
        )

        val back_main_btn = findViewById<Button>(R.id.back_main_btn)
        back_main_btn.setOnClickListener{
            startActivity(Intent(this, FirstPage::class.java))
            this.finish()
        }


        val change_btn = findViewById<Button>(R.id.change_btn)
        change_btn.setOnClickListener{
            startActivity(Intent(this, InfoChangePage::class.java))
            this.finish()
        }
    }
}