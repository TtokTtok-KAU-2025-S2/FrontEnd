package com.kau.ttokttok.ui.xml.calendar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * 소음 측정을 위한 원형 게이지 커스텀 뷰
 * - 0~80dB 범위 표시
 * - dB 값에 따른 색상 변화 (초록 -> 노랑 -> 주황 -> 빨강)
 * - 부드러운 애니메이션 효과
 */
class CircularGaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 배경 게이지 (회색 반투명)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = 0x4DFFFFFF // 30% 불투명 흰색
        strokeCap = Paint.Cap.ROUND
    }

    // 진행 상태 게이지 (색상 동적 변경)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = 0xFFFFFFFF.toInt() // 흰색
        strokeCap = Paint.Cap.ROUND
    }

    private val bounds = RectF()
    private var currentProgress = 0f // 현재 표시 중인 dB 값
    private var targetProgress = 0f // 목표 dB 값
    private var animator: ValueAnimator? = null

    // dB 범위 설정 (0 ~ 80dB) - 일반 생활 소음 기준
    private val minDb = 0f
    private val maxDb = 80f

    // 게이지 각도 설정 (270도 게이지)
    private val startAngle = 135f // 왼쪽 하단에서 시작 (7시 방향)
    private val sweepAngle = 270f // 270도 호 (시계방향으로 5시 방향까지)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (minOf(width, height) / 2f) - backgroundPaint.strokeWidth

        // 원형 게이지의 영역 설정
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

    /**
     * dB 값에 따른 색상 반환
     * - 35dB 미만: 초록 (매우 조용, 야간 기준 이하)
     * - 35~40dB: 연두 (조용, 주간 기준 이하)
     * - 40~45dB: 노랑 (보통, 공기전달 소음 기준)
     * - 45~60dB: 주황 (시끄러움, 일반 대화 수준)
     * - 60dB 이상: 빨강 (매우 시끄러움)
     */
    private fun getColorForDb(db: Float): Int {
        return when {
            db < 35 -> 0xFF4CAF50.toInt() // 초록색
            db < 40 -> 0xFF8BC34A.toInt() // 연두색
            db < 45 -> 0xFFFFC107.toInt() // 노란색
            db < 60 -> 0xFFFF9800.toInt() // 주황색
            else -> 0xFFF44336.toInt() // 빨간색
        }
    }

    /**
     * 게이지 진행 상태 설정
     * @param db 표시할 데시벨 값 (0~80)
     * @param animate true면 애니메이션 적용, false면 즉시 변경
     */
    fun setProgress(db: Float, animate: Boolean = true) {
        targetProgress = db.coerceIn(minDb, maxDb)

        if (animate) {
            animator?.cancel()
            animator = ValueAnimator.ofFloat(currentProgress, targetProgress).apply {
                duration = 300 // 0.3초 애니메이션
                interpolator = DecelerateInterpolator() // 감속 효과
                addUpdateListener { animation ->
                    currentProgress = animation.animatedValue as Float
                    invalidate() // 화면 다시 그리기
                }
                start()
            }
        } else {
            currentProgress = targetProgress
            invalidate()
        }
    }

    /**
     * 게이지 초기화 (0으로 리셋)
     */
    fun reset() {
        animator?.cancel()
        currentProgress = 0f
        targetProgress = 0f
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // 뷰가 제거될 때 애니메이터 정리
        animator?.cancel()
    }
}
