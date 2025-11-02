package com.kau.ttokttok.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kau.ttokttok.R
import com.kau.ttokttok.databinding.ItemNoiseLogBinding
import com.kau.ttokttok.domain.model.NoiseLog
import java.text.SimpleDateFormat
import java.util.Locale

// 소음 일기 목록을 표시하는 RecyclerView 어댑터
//
// @param onDeleteClick 삭제 버튼 클릭 콜백
// @param onEditClick 수정 버튼 클릭 콜백
// @param onCheckChanged 체크박스 상태 변경 콜백
class NoiseLogAdapter(
    private val onDeleteClick: (NoiseLog) -> Unit,
    private val onEditClick: (NoiseLog) -> Unit,
    private val onCheckChanged: (NoiseLog) -> Unit
) : ListAdapter<NoiseLog, NoiseLogAdapter.ViewHolder>(DiffCallback()) {

    // 새로운 ViewHolder 생성 - ViewBinding 사용
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoiseLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    // ViewHolder에 데이터 바인딩
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // 소음 일기 아이템을 표시하는 ViewHolder
    // ViewBinding을 사용하여 UI 요소에 접근
    inner class ViewHolder(
        private val binding: ItemNoiseLogBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        // 날짜 포맷터 (예: 11월 02일 16:49)
        private val dateFormat = SimpleDateFormat("MM월 dd일 HH:mm", Locale.KOREAN)

        // NoiseLog 데이터를 UI에 바인딩
        fun bind(log: NoiseLog) {
            // 날짜 표시
            binding.tvDate.text = dateFormat.format(log.measuredAt)

            // 리포트 생성 상태 표시
            if (log.hasReport) {
                binding.tvReportStatus.visibility = android.view.View.VISIBLE
                binding.tvReportStatus.text = "📄 리포트 생성됨"
            } else {
                binding.tvReportStatus.visibility = android.view.View.GONE
            }

            // 소음 레벨 배지 표시 (매우 시끄러움, 시끄러움 등)
            val level = getNoiseLevel(log.maxDecibel)
            binding.tvNoiseLevel.text = level.first
            binding.tvNoiseLevel.setBackgroundResource(level.second)

            // dB 정보 표시
            binding.tvMaxDb.text = "${log.maxDecibel.toInt()}dB"
            binding.tvAvgDb.text = "${log.avgDecibel.toInt()}dB"

            // 소음 유형 표시 (발걸음, 망치질 등)
            binding.tvNoiseType.text = log.noiseType

            // 메모 내용 표시
            binding.tvMemo.text = log.memo

            // 체크박스 설정 (리포트 생성 여부)
            binding.cbSelect.setOnCheckedChangeListener(null) // 기존 리스너 제거
            binding.cbSelect.isChecked = log.hasReport
            binding.cbSelect.setOnCheckedChangeListener { _, _ ->
                onCheckChanged(log)
            }

            // 메뉴 버튼 (수정/삭제) 설정
            binding.btnMenu.setOnClickListener {
                showPopupMenu(it, log)
            }
        }

        // 수정/삭제 팝업 메뉴 표시
        private fun showPopupMenu(view: android.view.View, log: NoiseLog) {
            PopupMenu(view.context, view).apply {
                inflate(R.menu.menu_noise_log_item)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_edit -> {
                            onEditClick(log)
                            true
                        }
                        R.id.action_delete -> {
                            onDeleteClick(log)
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }

        // dB 값에 따라 소음 레벨 텍스트와 배경 리소스 반환
        private fun getNoiseLevel(db: Double): Pair<String, Int> {
            return when {
                db >= 85.0 -> "매우 시끄러움" to R.drawable.bg_level_red
                db >= 70.0 -> "시끄러움" to R.drawable.bg_level_orange
                db >= 50.0 -> "보통" to R.drawable.bg_level_yellow
                else -> "조용함" to R.drawable.bg_level_green
            }
        }
    }

    // DiffUtil 콜백 - 리스트 변경 시 효율적인 업데이트를 위한 비교 로직
    private class DiffCallback : DiffUtil.ItemCallback<NoiseLog>() {
        // 같은 아이템인지 비교 (ID로 비교)
        override fun areItemsTheSame(oldItem: NoiseLog, newItem: NoiseLog): Boolean {
            return oldItem.id == newItem.id
        }

        // 내용이 같은지 비교 (모든 필드 비교)
        override fun areContentsTheSame(oldItem: NoiseLog, newItem: NoiseLog): Boolean {
            return oldItem == newItem
        }
    }
}
