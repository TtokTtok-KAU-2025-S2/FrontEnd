package com.kau.ttokttok.ui.calendar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

class CircularGaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = 0x4DFFFFFF // 30% 불투명 흰색
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = 0xFFFFFFFF.toInt() // 흰색
        strokeCap = Paint.Cap.ROUND
    }

    private val bounds = RectF()
    private var currentProgress = 0f // 0 ~ 80 (dB 값)
    private var targetProgress = 0f
    private var animator: ValueAnimator? = null

    // dB 범위 설정 (0 ~ 80dB) - 일반 생활 소음 기준
    private val minDb = 0f
    private val maxDb = 80f

    // 게이지 각도 설정 (270도 게이지)
    private val startAngle = 135f // 왼쪽 하단에서 시작
    private val sweepAngle = 270f // 270도 게이지

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (minOf(width, height) / 2f) - backgroundPaint.strokeWidth

        bounds.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        // 배경 원형 게이지 그리기
        canvas.drawArc(bounds, startAngle, sweepAngle, false, backgroundPaint)

        // 현재 진행 상태에 따른 게이지 그리기
        if (currentProgress > 0) {
            val progressSweep = (currentProgress / maxDb) * sweepAngle

            // dB 값에 따라 색상 변경
            progressPaint.color = getColorForDb(currentProgress)

            canvas.drawArc(bounds, startAngle, progressSweep, false, progressPaint)
        }
    }

    private fun getColorForDb(db: Float): Int {
        return when {
            db < 35 -> 0xFF4CAF50.toInt() // 초록색 - 매우 조용함 (야간 기준 이하)
            db < 40 -> 0xFF8BC34A.toInt() // 연두색 - 조용함 (주간 기준 이하)
            db < 45 -> 0xFFFFC107.toInt() // 노란색 - 보통 (공기전달 소음 주간 기준)
            db < 60 -> 0xFFFF9800.toInt() // 주황색 - 시끄러움 (일반 대화 수준)
            else -> 0xFFF44336.toInt() // 빨간색 - 매우 시끄러움 (60dB 이상)
        }
    }

    fun setProgress(db: Float, animate: Boolean = true) {
        targetProgress = db.coerceIn(minDb, maxDb)

        if (animate) {
            animator?.cancel()
            animator = ValueAnimator.ofFloat(currentProgress, targetProgress).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
                addUpdateListener { animation ->
                    currentProgress = animation.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            currentProgress = targetProgress
            invalidate()
        }
    }

    fun reset() {
        animator?.cancel()
        currentProgress = 0f
        targetProgress = 0f
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}

