package com.kau.ttokttok.ui.xml.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

/**
 * 배경에 별빛 효과를 주는 커스텀 뷰
 * XML 레이아웃에서 사용 가능
 * Jetpack Compose의 StarField와 동일한 효과 제공
 */
class StarFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val stars: List<Star>
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 별 데이터 클래스
     * @param x 화면 내 x 좌표 비율 (0.0 ~ 1.0)
     * @param y 화면 내 y 좌표 비율 (0.0 ~ 1.0)
     * @param radius 별의 크기 (픽셀)
     * @param alpha 투명도 (0.5 ~ 1.0)
     */
    data class Star(
        val x: Float,
        val y: Float,
        val radius: Float,
        val alpha: Float
    )

    init {
        // 200개의 랜덤 별 생성 (초기화 시 한 번만)
        stars = List(200) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 2f + 0.5f, // 0.5 ~ 2.5 픽셀
                alpha = Random.nextFloat() * 0.5f + 0.5f // 0.5 ~ 1.0 투명도
            )
        }

        // 전체 뷰에 35% 투명도 적용 (Compose와 동일)
        alpha = 0.35f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        // 모든 별 그리기
        stars.forEach { star ->
            paint.color = android.graphics.Color.WHITE
            paint.alpha = (star.alpha * 255).toInt()

            canvas.drawCircle(
                star.x * w, // 비율을 실제 픽셀로 변환
                star.y * h,
                star.radius,
                paint
            )
        }
    }
}
