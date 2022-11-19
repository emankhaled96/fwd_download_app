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
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private val rect = RectF(50f, 50f, 900f, 700f)
    private val smallRect = RectF(650f, 110f, 700f, 160f)

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

    var progress = 0


    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
        onButtonStateChanged(new)

    }

    private fun onButtonStateChanged(buttonState: ButtonState) {
        when (buttonState) {
            ButtonState.Clicked -> {
                valueAnimator = ValueAnimator()

                valueAnimator.setIntValues(
                    50, 900
                )
                valueAnimator.setEvaluator(IntEvaluator())
                valueAnimator.addUpdateListener { valueAnimator ->
                    progress = valueAnimator.animatedValue as Int
                    Log.d("progress", progress.toString())
                    invalidate()
                }
                valueAnimator.duration = 1000
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
        canvas?.drawRect(rect, backgroundPaint.apply { resources.getColor(R.color.colorPrimary) })

        if (buttonState == ButtonState.Clicked) {

            canvas?.drawRect(
                50f,
                50f,
                progress.toFloat(),
                measuredHeight.toFloat(),
                backgroundPaint.apply { color = resources.getColor(R.color.colorPrimaryDark) })

            canvas?.drawArc(
                smallRect,
                0f,
                360f,
                true,
                backgroundPaint.apply { color = resources.getColor(R.color.colorAccent) })
            canvas?.drawText(
                context.getString(R.string.button_loading),
                460f,
                150f,
                textPaint.apply { color = Color.WHITE })

        } else {

            canvas?.drawText(context.getString(R.string.download), 460f, 150f, textPaint.apply { color = Color.WHITE })

        }
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