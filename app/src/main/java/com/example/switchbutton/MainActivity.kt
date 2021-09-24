package com.example.switchbutton

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.example.switchbutton.databinding.ActivityMainBinding
import com.example.switchbutton.databinding.ComponentSwitchButtomBinding

class MainActivity : AppCompatActivity() , View.OnTouchListener,View.OnClickListener{
    val TAG = "MainActivity"
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        binding = ActivityMainBinding.bind(rootView)
        setContentView(binding.root)
        binding.mySwitcher.setChooseLeftOrRight(0)
        Log.e(TAG,"2" +
                "3332")

//        binding.mySwitcher.setOnTouchListener(this)
        binding.mySwitcher.setOnClickListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event == null) return true
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                Log.e(TAG,binding.mySwitcher.getChoose().toString())
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        Log.e(TAG,binding.mySwitcher.getChoose().toString())
    }


}
