package com.milet0819.notificationtest.ui.lottery

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.milet0819.notificationtest.R
import com.milet0819.notificationtest.databinding.ActivityDrawingBinding
import com.milet0819.notificationtest.databinding.ActivityLotteryBinding

class LotteryActivity : AppCompatActivity() {

    val binding: ActivityLotteryBinding by lazy {
        ActivityLotteryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
}