package com.zl.timefly

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

class ClockView : View {

    companion object {
        private val TAG = ClockView::class.java.simpleName
    }

    private lateinit var mPaint: Paint
    private lateinit var mTextPaint: Paint
    private lateinit var mRect: RectF

    private var textSize = 8f
    private var maxStockWidth = 4f

    var bgColor:Int = Color.WHITE
    var textColor:Int = Color.WHITE
    var pointColor:Int = Color.BLACK

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        textSize *= context.resources.displayMetrics.density
        maxStockWidth *= context.resources.displayMetrics.density

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = textSize

        mRect = RectF()

        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.ClockView, defStyle, 0)

        bgColor = a.getColor(R.styleable.ClockView_bgColor, Color.RED)
        textColor = a.getColor(R.styleable.ClockView_textColor, Color.RED)
        pointColor = a.getColor(R.styleable.ClockView_pointColor, Color.RED)

        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)

        val width = 200
        val height = 200

        setMeasuredDimension(if (widthMode == MeasureSpec.EXACTLY)
            sizeWidth
        else
            width, if (heightMode == MeasureSpec.EXACTLY)
            sizeHeight
        else
            height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rad = if (measuredHeight > measuredWidth) {
            measuredWidth / 2
        } else {
            measuredHeight / 2
        }

        mRect.left = (measuredWidth / 2 - rad).toFloat()
        mRect.top = (measuredHeight / 2 - rad).toFloat()
        mRect.right = (measuredWidth / 2 + rad).toFloat()
        mRect.bottom = (measuredHeight / 2 + rad).toFloat()
        mPaint.color = bgColor
        mPaint.strokeWidth = maxStockWidth
        canvas.drawOval(mRect, mPaint)

        mTextPaint.color = textColor
        for (i in 1..60) {
//            Log.i(TAG, "onDraw: $i")
            canvas.save()
            canvas.rotate(6f * i, measuredWidth / 2f, measuredHeight / 2f)
            if (i % 5 == 0) {
                canvas.drawText((i / 5).toString(), measuredWidth / 2f, measuredHeight / 2f - rad + textSize, mTextPaint)
            } else {
                canvas.drawLine(measuredWidth / 2f,
                        measuredHeight / 2f - rad,
                        measuredWidth / 2f,
                        measuredHeight / 2f - rad + textSize / 2,
                        mTextPaint)
            }
            canvas.restore()
        }

        val cal = Calendar.getInstance()
        val second = cal.get(Calendar.SECOND)
        val minute = cal.get(Calendar.MINUTE) + second / 60f
        val hour = cal.get(Calendar.HOUR) + minute / 60f

        mPaint.color = pointColor
        var stopX = (measuredWidth / 2 + Math.sin(hour / 6.0 * Math.PI) * rad / 2).toFloat()
        var stopY = (measuredHeight / 2 - Math.cos(hour / 6.0 * Math.PI) * rad / 2).toFloat()
        canvas.drawLine((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(), stopX, stopY, mPaint)

        mPaint.strokeWidth = maxStockWidth * 2 / 3
        stopX = (measuredWidth / 2 + Math.sin(minute / 30.0 * Math.PI) * 2 * rad / 3).toFloat()
        stopY = (measuredHeight / 2 - Math.cos(minute / 30.0 * Math.PI) * 2 * rad / 3).toFloat()
        canvas.drawLine((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(), stopX, stopY, mPaint)

        mPaint.strokeWidth = maxStockWidth / 2
        stopX = (measuredWidth / 2 + Math.sin(second / 30.0 * Math.PI) * 5 * rad / 6).toFloat()
        stopY = (measuredHeight / 2 - Math.cos(second / 30.0 * Math.PI) * 5 * rad / 6).toFloat()
        canvas.drawLine((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(), stopX, stopY, mPaint)

        postInvalidateDelayed(1000)
    }

}
