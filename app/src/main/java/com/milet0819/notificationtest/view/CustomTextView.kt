package com.milet0819.notificationtest.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.DragEvent
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintSet.Motion
import com.milet0819.notificationtest.common.utils.logger


class CustomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var drawPaint: Paint
    private var eraserPaint: Paint

//    private lateinit var bitmap: Bitmap
    private var drawPath: Path
    private var erasePath: Path

    init {
        setUpperText()

        drawPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
        }

        eraserPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 50f
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }

        drawPath = Path()
        erasePath = Path()
    }

    private fun setUpperText() {
        text = text.toString().uppercase()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        logger("w=$w, h=$h, oldw=$oldw, oldh=$oldh")
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                erasePath.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                erasePath.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                erasePath.lineTo(x, y)
            }
        }
        logger(event)
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        logger("canvas=$canvas")
        super.onDraw(canvas)

        val bounds = RectF(x, y, width.toFloat(), height.toFloat())

        canvas.apply {
            drawRect(bounds, drawPaint)
            drawPath(erasePath, eraserPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        logger("width=$widthMeasureSpec height=$heightMeasureSpec")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}