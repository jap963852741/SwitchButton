package com.example.switchbutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.switchbutton.databinding.ActivityMainBinding
import com.example.switchbutton.databinding.ComponentSwitchButtomBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        val binding = ActivityMainBinding.bind(rootView)
        setContentView(binding.root)
        binding.mySwitcher.setDefaultChooseLeftOrRight(1)

    }
}