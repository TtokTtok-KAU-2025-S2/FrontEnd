package com.kau.ttokttok.ui.xml.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

/**
 * XML 레이아웃에서 사용 가능한 별빛 배경 커스텀 뷰
 * Compose의 StarField와 동일한 효과를 제공
 */
class StarFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val stars: List<Star>
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    data class Star(
        val x: Float,
        val y: Float,
        val radius: Float,
        val alpha: Float
    )

    init {
        // 200개의 랜덤 별 생성
        stars = List(200) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 2f + 0.5f,
                alpha = Random.nextFloat() * 0.5f + 0.5f
            )
        }

        // 투명도 35% 적용 (Compose와 동일)
        alpha = 0.35f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        stars.forEach { star ->
            paint.color = android.graphics.Color.WHITE
            paint.alpha = (star.alpha * 255).toInt()

            canvas.drawCircle(
                star.x * w,
                star.y * h,
                star.radius,
                paint
            )
        }
    }
}

