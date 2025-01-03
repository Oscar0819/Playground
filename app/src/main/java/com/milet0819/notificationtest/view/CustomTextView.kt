package com.milet0819.notificationtest.view

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import com.milet0819.notificationtest.common.utils.logger


class CustomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setUpperText()
    }

    private fun setUpperText() {
        text = text.toString().uppercase()
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        logger("text=$text")
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        logger("$event")
        return super.onTouchEvent(event)
    }



}