package com.example.fingerchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repalceFragment(FriendsList())
        var btn1 = findViewById<Button>(R.id.btn1)
        var btn2 = findViewById<Button>(R.id.btn2)
        btn1.setOnClickListener{
            repalceFragment(SearchFriends())
        }
        btn2.setOnClickListener{
            repalceFragment(FriendsList())
        }

//        HiOkHttp.getAsync("http://wsjw.xyz/")
        HiOkHttp.postAsync("http://wsjw.xyz/login/")
    }

    fun repalceFragment(fragment: Fragment){
        var fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        var oldfragment = fragmentManager.findFragmentById(R.id.fL)
        if(oldfragment ==null || oldfragment != fragment){
            transaction.add(R.id.fL,fragment)
        }
        var ls = fragmentManager.fragments
        if(ls != null){
            ls.forEach{
                transaction.hide(it)
            }
        }
        transaction.show(fragment)
        transaction.commit()
    }
}