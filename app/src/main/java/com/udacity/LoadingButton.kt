package com.udacity

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0


    private var valueAnimator = ValueAnimator()
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    var progress = 0F


    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
        onButtonStateChanged(new)

    }

    private fun onButtonStateChanged(buttonState: ButtonState) {
        when (buttonState) {
            ButtonState.Clicked -> {
                valueAnimator = ValueAnimator.ofFloat(0f, 1f)

                valueAnimator.addUpdateListener { valueAnimator ->
                    progress = valueAnimator.animatedValue as Float
                    Log.d("progress", progress.toString())
                    invalidate()
                }
                valueAnimator.duration = 1500
                valueAnimator.repeatCount = ValueAnimator.INFINITE
                valueAnimator.start()

            }
            ButtonState.Completed -> {
                valueAnimator.end()
                setColor(resources.getColor(R.color.colorPrimary))


            }
            else -> {
                setColor(resources.getColor(R.color.colorPrimary))
            }
        }
    }

    private fun setColor(color: Int) {
        backgroundPaint.color = color
        invalidate()
    }

    init {

        isClickable = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(ContextCompat.getColor(context, R.color.colorPrimary))
        if (buttonState == ButtonState.Clicked) {
            drawAnimation(canvas)

        } else {
            drawUI(canvas)
        }
    }

    fun drawUI(canvas: Canvas?) {
        canvas?.drawRect(
            0f, 0f, widthSize.toFloat(), heightSize.toFloat(),
            backgroundPaint.apply { resources.getColor(R.color.colorPrimary) })

        canvas?.drawText(
            context.getString(R.string.download),
            widthSize.toFloat() / 2,
            ((heightSize.toFloat() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)),

            textPaint.apply { color = Color.WHITE })

    }

    fun drawAnimation(canvas: Canvas?) {
        // Draw Rect
        canvas?.drawRect(
            0f,
            0f,
            progress * widthSize.toFloat(),
            measuredHeight.toFloat(),
            backgroundPaint.apply { color = resources.getColor(R.color.colorPrimaryDark) })

// Draw Text
        canvas?.drawText(
            context.getString(R.string.button_loading),
            widthSize.toFloat() / 2,
            ((heightSize.toFloat() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)),

            textPaint.apply { color = Color.WHITE })
// Draw animated Circle
        canvas?.drawArc(
            675f, 80f, 725f, 130f,
            0f,
            progress * 360f,
            true,
            backgroundPaint.apply { color = resources.getColor(R.color.colorAccent) })

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


}