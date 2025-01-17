package com.milet0819.notificationtest.ui.drawing

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.milet0819.notificationtest.R
import com.milet0819.notificationtest.common.utils.logger
import com.milet0819.notificationtest.databinding.ActivityDrawingBinding
import com.milet0819.notificationtest.databinding.ActivityMainBinding

class DrawingActivity : AppCompatActivity() {
    companion object {
        const val CODE = "code"
        const val NAME = "name"
    }

    val binding: ActivityDrawingBinding by lazy {
        ActivityDrawingBinding.inflate(layoutInflater)
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

        initView()

        val code = intent.extras?.getInt(CODE)
        val name = intent.extras?.getString(NAME)

        logger("Success code=$code, name=$name")
    }

    private fun initView() {
        binding.sEraseMode.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.dv.setEraserMode(isChecked)
        }
    }
}