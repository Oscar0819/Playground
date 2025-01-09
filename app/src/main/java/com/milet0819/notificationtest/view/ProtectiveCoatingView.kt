package com.milet0819.notificationtest.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class ProtectiveCoatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {
    private var paint: Paint
    private var eraserPaint: Paint
    private var canvas: Canvas? = null
    private var bitmap: Bitmap? = null

    init {
        paint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
        }

        eraserPaint = Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        canvas = Canvas(bitmap!!)
        canvas?.drawRect(0F, 0F, w.toFloat(), h.toFloat(), paint)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        bitmap?.let { bm ->
            canvas.drawBitmap(bm, 0F, 0F, null)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                x = event.x
                y = event.y
                canvas?.drawCircle(x, y, 50F, eraserPaint)

                invalidate()
                return true
            }
        }

        return super.onTouchEvent(event)
    }

}