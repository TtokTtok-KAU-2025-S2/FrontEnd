package com.kau.ttokttok.ui.xml.calendar

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

// TODO: [백엔드 연동] 이미지나 첨부파일이 추가되는 경우 Glide/Coil로 이미지 로딩 필요
class NoiseLogAdapter(
    private val onDeleteClick: (NoiseLog) -> Unit,
    private val onEditClick: (NoiseLog) -> Unit,
    private val onItemCheckChanged: (NoiseLog, Boolean) -> Unit
) : ListAdapter<NoiseLog, NoiseLogAdapter.ViewHolder>(DiffCallback()) {

    private val selectedItems = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoiseLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getSelectedLogs(): List<NoiseLog> {
        return currentList.filter { log ->
            log.id in selectedItems && !log.hasReport
        }
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemNoiseLogBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("MM월 dd일 HH:mm", Locale.KOREAN)

        fun bind(log: NoiseLog) {
            binding.tvDate.text = dateFormat.format(log.measuredAt)

            // TODO: [백엔드 연동] 서버의 리포트 생성 상태(hasReport)와 동기화
            // 리포트 생성 상태 표시
            binding.tvReportStatus.visibility = if (log.hasReport) {
                binding.tvReportStatus.text = "📄 리포트 생성됨"
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }

            // 소음 레벨 배지 표시
            val (levelText, levelBg) = getNoiseLevel(log.maxDecibel)
            binding.tvNoiseLevel.text = levelText
            binding.tvNoiseLevel.setBackgroundResource(levelBg)

            // dB 정보 표시
            binding.tvMaxDb.text = "${log.maxDecibel.toInt()}dB"
            binding.tvAvgDb.text = "${log.avgDecibel.toInt()}dB"
            binding.tvNoiseType.text = log.noiseType
            binding.tvMemo.text = log.memo

            // 체크박스 설정
            setupCheckbox(log)

            // 메뉴 버튼 설정
            binding.btnMenu.setOnClickListener {
                showPopupMenu(it, log)
            }
        }

        private fun setupCheckbox(log: NoiseLog) {
            binding.cbSelect.setOnCheckedChangeListener(null)

            if (log.hasReport) {
                // TODO: [백엔드 연동] 서버의 리포트 상태 확인 후 체크박스 활성화/비활성화
                // 이미 리포트가 생성된 항목은 체크 표시하고 비활성화
                binding.cbSelect.isChecked = true
                binding.cbSelect.isEnabled = false
            } else {
                // 아직 리포트 미생성 항목은 활성화
                binding.cbSelect.isEnabled = true
                binding.cbSelect.isChecked = log.id in selectedItems

                binding.cbSelect.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (buttonView.isPressed) {
                        log.id?.let { id ->
                            if (isChecked) {
                                selectedItems.add(id)
                            } else {
                                selectedItems.remove(id)
                            }
                            onItemCheckChanged(log, isChecked)
                        }
                    }
                }
            }
        }

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

        private fun getNoiseLevel(db: Double): Pair<String, Int> = when {
            db >= 85.0 -> "매우 시끄러움" to R.drawable.bg_level_red
            db >= 70.0 -> "시끄러움" to R.drawable.bg_level_orange
            db >= 50.0 -> "보통" to R.drawable.bg_level_yellow
            else -> "조용함" to R.drawable.bg_level_green
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<NoiseLog>() {
        override fun areItemsTheSame(oldItem: NoiseLog, newItem: NoiseLog): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoiseLog, newItem: NoiseLog): Boolean {
            return oldItem == newItem
        }
    }
}

