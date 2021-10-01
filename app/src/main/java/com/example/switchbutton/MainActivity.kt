package com.example.switchbutton

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , View.OnTouchListener,View.OnClickListener{
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mySwitcher.setOnTouchListener(this)
        mySwitcher.setOnClickListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event == null) return true
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                //會是按下去的那個瞬間的值
                Log.e(TAG,"onTouch is : ${mySwitcher.getChoose()}")
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        Log.e(TAG,"onClick is : ${mySwitcher.getChoose()}")
    }


}
