package com.milet0819.notificationtest.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.milet0819.notificationtest.common.utils.logger

/**
 * Note :
 *
 */
class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {
    var isEraseMode = false

    private var drawPaint: Paint
    private var drawPath: Path
    private var drawCanvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null

    init {
        /**
         * setXfermode에 의해 생긴 검은 선을 제가하기 위해 추가한 옵션.
         * 이유 확인 필요.
         */
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        drawPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 66F
        }

        drawPath = Path()
    }

    /**
     * orientation 변경에 따른 로직 추가 필요.
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvasBitmap?.let { bitmap ->
            drawCanvas = Canvas(bitmap)
        }

        logger("w=$w, h=$h, oldw=$oldw, oldh=$oldh")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            canvasBitmap?.let { bitmap ->
                canvas.drawBitmap(bitmap, 0F, 0F, null)
            }
            drawPath(drawPath, drawPaint)
//            drawPath(erasePath, eraserPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                drawCanvas?.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
        }
        logger(event)

        invalidate()
        return true
    }

    fun setEraserMode(isEraser: Boolean) {
        if (isEraser) {
            drawPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
            drawPaint.alpha = 0
        } else {
            drawPaint.setXfermode(null)
            drawPaint.alpha = 255
        }
    }
}