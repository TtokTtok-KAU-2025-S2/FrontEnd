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
            setupStats(apartment.noiseDistribution)
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

        // 통계 정보 (총 건수, 소음 레벨) - 실제 리포트 건수 기반으로 소음 정도 판단
        private fun setupStats(distribution: NoiseDistribution) {
            val total = getTotalCount(distribution)

            // 실제 리포트 건수를 기반으로 소음 레벨 판단
            val (levelText, colorRes) = when {
                total >= 50 -> "시끄러움" to R.color.db_high
                total >= 20 -> "보통" to R.color.db_moderate
                else -> "조용함" to R.color.db_safe
            }

            binding.tvTotalReports.text = "${total}건"
            binding.tvNoiseLevel.text = levelText
            binding.tvNoiseLevel.setTextColor(binding.root.context.getColor(colorRes))
        }

        // 파이 차트 설정
        private fun setupChart(distribution: NoiseDistribution) {
            val entries = mutableListOf<PieEntry>()
            val colors = mutableListOf<Int>()
            val context = binding.root.context

            getNoiseTypes(distribution).forEach { type ->
                if (type.count > 0) {
                    entries.add(PieEntry(type.count.toFloat(), type.label))
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

            getNoiseTypes(distribution).forEach { type ->
                if (type.count > 0) {
                    val legendBinding = ItemLegendBinding.inflate(
                        inflater,
                        binding.legendContainer,
                        false
                    )
                    legendBinding.tvLegendLabel.text = "${type.label} (${type.count})"
                    legendBinding.colorIndicator.setBackgroundColor(context.getColor(type.colorRes))
                    binding.legendContainer.addView(legendBinding.root)
                }
            }
        }

        // 총 리포트 건수
        private fun getTotalCount(d: NoiseDistribution) =
            (d.FOOTSTEPS ?: 0) + (d.FURNITURE ?: 0) +
            (d.HAMMERING ?: 0) + (d.MUSIC ?: 0) + (d.UNKNOWN ?: 0)

        // 노이즈 타입별 데이터
        private fun getNoiseTypes(d: NoiseDistribution) = listOf(
            NoiseType("발걸음", d.FOOTSTEPS ?: 0, R.color.chart_blue),
            NoiseType("가구", d.FURNITURE ?: 0, R.color.chart_green),
            NoiseType("망치질", d.HAMMERING ?: 0, R.color.chart_orange),
            NoiseType("음악", d.MUSIC ?: 0, R.color.chart_purple),
            NoiseType("기타", d.UNKNOWN ?: 0, R.color.chart_gray)
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
