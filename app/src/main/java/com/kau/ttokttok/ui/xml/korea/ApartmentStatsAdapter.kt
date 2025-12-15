package com.kau.ttokttok.ui.xml.korea

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.kau.ttokttok.R
import com.kau.ttokttok.data.remote.dto.report.res.Apartment
import com.kau.ttokttok.data.remote.dto.report.res.NoiseDistribution
import com.kau.ttokttok.databinding.ItemApartmentDataBinding
import com.kau.ttokttok.databinding.ItemLegendBinding

// 노이즈 타입 데이터
private data class NoiseType(val label: String, val count: Int, val colorRes: Int)

// 아파트 통계 RecyclerView 어댑터
class ApartmentStatsAdapter : ListAdapter<Apartment, ApartmentStatsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemApartmentDataBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemApartmentDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(apartment: Apartment) {
            binding.tvApartmentName.text = apartment.apartmentName

            setupBadge(apartment.status)
            setupChart(apartment.noiseDistribution)
            setupLegend(apartment.noiseDistribution)
        }

        // 상태 배지 (HIGH/MEDIUM/LOW) - 앱 사용량/활성화 여부
        private fun setupBadge(status: String) {
            val (text, bgRes) = when (status) {
                "HIGH" -> "앱 활성도 : 상" to R.drawable.bg_badge_green
                "MEDIUM" -> "앱 활성도 : 중" to R.drawable.bg_badge_orange
                "LOW" -> "앱 활성도 : 하" to R.drawable.bg_badge_red
                else -> return binding.tvActiveBadge.run { visibility = View.GONE }
            }

            binding.tvActiveBadge.apply {
                visibility = View.VISIBLE
                this.text = text
                setBackgroundResource(bgRes)
            }
        }

        // 파이 차트 설정
        private fun setupChart(distribution: NoiseDistribution) {
            val entries = mutableListOf<PieEntry>()
            val colors = mutableListOf<Int>()
            val context = binding.root.context
            val total = getTotalCount(distribution)

            if (total == 0) {
                binding.pieChart.visibility = View.GONE
                return
            }

            // 0이 아닌 항목들만 파이 차트에 추가
            getNoiseTypes(distribution).forEach { type ->
                if (type.count > 0) {
                    val percentage = (type.count.toFloat() / total * 100)
                    entries.add(PieEntry(percentage, type.label))
                    colors.add(context.getColor(type.colorRes))
                }
            }

            if (entries.isEmpty()) {
                binding.pieChart.visibility = View.GONE
                return
            }

            binding.pieChart.visibility = View.VISIBLE

            val dataSet = PieDataSet(entries, "").apply {
                setColors(colors)
                valueTextColor = Color.WHITE
                valueTextSize = 12f
                sliceSpace = 3f
                selectionShift = 5f
                valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return if (value > 1f) "${value.toInt()}%" else ""
                    }
                }
            }

            binding.pieChart.apply {
                data = PieData(dataSet)
                description.isEnabled = false
                legend.isEnabled = false
                setDrawEntryLabels(false)
                holeRadius = 40f
                transparentCircleRadius = 45f
                setHoleColor(Color.TRANSPARENT)
                animateY(1000, Easing.EaseInOutQuad)
                invalidate()
            }
        }

        // 범례 동적 생성
        private fun setupLegend(distribution: NoiseDistribution) {
            binding.legendContainer.removeAllViews()
            val context = binding.root.context
            val inflater = LayoutInflater.from(context)
            val total = getTotalCount(distribution)

            if (total == 0) return

            // getNoiseTypes와 동일한 순서로 범례 생성
            // 2열 레이아웃: 짝수 인덱스면 왼쪽, 홀수 인덱스면 오른쪽
            var leftContainer: ViewGroup? = null

            getNoiseTypes(distribution).forEachIndexed { index, type ->
                if (type.count > 0) {
                    // 2개씩 묶어서 행 생성
                    if (index % 2 == 0) {
                        leftContainer = android.widget.LinearLayout(context).apply {
                            layoutParams = android.widget.LinearLayout.LayoutParams(
                                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            orientation = android.widget.LinearLayout.HORIZONTAL
                            binding.legendContainer.addView(this)
                        }
                    }

                    val percentage = (type.count.toFloat() / total * 100).toInt()
                    val legendBinding = ItemLegendBinding.inflate(
                        inflater,
                        leftContainer,
                        false
                    )
                    legendBinding.tvLegendLabel.text = "${type.label} (${percentage}%)"
                    legendBinding.colorIndicator.setBackgroundColor(context.getColor(type.colorRes))

                    // 너비를 50%로 설정하여 2열 레이아웃 구성
                    val params = android.widget.LinearLayout.LayoutParams(
                        0,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    legendBinding.root.layoutParams = params
                    leftContainer?.addView(legendBinding.root)
                }
            }
        }

        // 총 리포트 건수
        private fun getTotalCount(d: NoiseDistribution) =
            (d.FOOTSTEPS ?: 0) + (d.HAMMERING ?: 0) + (d.FURNITURE ?: 0) +
            (d.MUSIC ?: 0) + (d.VOICE ?: 0) + (d.PET ?: 0) +
            (d.APPLIANCE ?: 0) + (d.DOOR ?: 0) + (d.WATER ?: 0) +
            (d.CONSTRUCTION ?: 0) + (d.EXERCISE ?: 0) + (d.UNKNOWN ?: 0)

        // 노이즈 타입별 데이터
        private fun getNoiseTypes(d: NoiseDistribution) = listOf(
            NoiseType("발걸음", d.FOOTSTEPS ?: 0, R.color.chart_blue),          // 파랑
            NoiseType("망치질", d.HAMMERING ?: 0, R.color.chart_red),           // 빨강
            NoiseType("가구", d.FURNITURE ?: 0, R.color.chart_green),           // 초록
            NoiseType("음악", d.MUSIC ?: 0, R.color.chart_purple),              // 보라
            NoiseType("고성방가", d.VOICE ?: 0, R.color.chart_pink),            // 분홍
            NoiseType("반려동물", d.PET ?: 0, R.color.chart_cyan),              // 청록
            NoiseType("가전제품", d.APPLIANCE ?: 0, R.color.chart_orange),      // 주황
            NoiseType("문", d.DOOR ?: 0, R.color.chart_indigo),                 // 인디고
            NoiseType("물", d.WATER ?: 0, R.color.chart_amber),                 // 황금색
            NoiseType("공사", d.CONSTRUCTION ?: 0, R.color.chart_lime),         // 라임
            NoiseType("운동기구", d.EXERCISE ?: 0, R.color.chart_rose),         // 장미색
            NoiseType("기타", d.UNKNOWN ?: 0, R.color.chart_gray)               // 회색
        )
    }

    // 리스트 변경 감지
    private class DiffCallback : DiffUtil.ItemCallback<Apartment>() {
        override fun areItemsTheSame(oldItem: Apartment, newItem: Apartment) =
            oldItem.apartmentId == newItem.apartmentId

        override fun areContentsTheSame(oldItem: Apartment, newItem: Apartment) =
            oldItem == newItem
    }
}
