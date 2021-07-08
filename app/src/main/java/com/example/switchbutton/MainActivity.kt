package com.example.switchbutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.example.switchbutton.databinding.ActivityMainBinding
import com.example.switchbutton.databinding.ComponentSwitchButtomBinding

class MainActivity : AppCompatActivity() , View.OnTouchListener{
    val TAG = "MainActivity"
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        binding = ActivityMainBinding.bind(rootView)
        setContentView(binding.root)
        binding.mySwitcher.setDefaultChooseLeftOrRight(1)

        binding.mySwitcher.setOnTouchListener(this)
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


}
