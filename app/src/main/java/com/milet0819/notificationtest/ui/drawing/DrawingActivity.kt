package com.milet0819.notificationtest.ui.drawing

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.milet0819.notificationtest.R
import com.milet0819.notificationtest.common.utils.logger

class DrawingActivity : AppCompatActivity() {
    companion object {
        const val CODE = "code"
        const val NAME = "name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_drawing)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val code = intent.extras?.getInt(CODE)
        val name = intent.extras?.getString(NAME)

        logger("Success code=$code, name=$name")

    }
}