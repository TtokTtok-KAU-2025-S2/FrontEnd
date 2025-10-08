package com.kau.ttokttok.ui.step2

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kau.ttokttok.R
import com.kau.ttokttok.databinding.ItemResponseBinding
import com.kau.ttokttok.domain.model.step2.NeighborResponse
import com.kau.ttokttok.domain.model.step2.enums.ResponseType
import java.text.SimpleDateFormat
import java.util.*

// 이웃들의 응답을 표시하는 RecyclerView 어댑터
class ResponseAdapter : ListAdapter<NeighborResponse, ResponseAdapter.ResponseViewHolder>(ResponseDiffCallback()) {

    // ViewHolder를 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder {
        val binding = ItemResponseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResponseViewHolder(binding)
    }

    // ViewHolder에 데이터를 바인딩하는 함수
    override fun onBindViewHolder(holder: ResponseViewHolder, position: Int) {
        val response = getItem(position)
        holder.bind(response)
    }

    // 각 응답 아이템을 표시하는 ViewHolder 클래스
    class ResponseViewHolder(private val binding: ItemResponseBinding) : RecyclerView.ViewHolder(binding.root) {

        // 응답 데이터를 뷰에 바인딩하는 함수
        fun bind(response: NeighborResponse) {
            // 이웃 위치 텍스트 설정
            binding.tvLocation.text = response.location

            // 응답 상태에 따라 아이콘, 텍스트, 색상 설정
            when (response.response) {
                ResponseType.HEARD -> {
                    binding.ivResponseIcon.setImageResource(R.drawable.ic_check_circle)
                    binding.ivResponseIcon.setColorFilter(Color.parseColor("#EF4444"))
                    binding.tvResponseText.text = "네, 저도 들려요"
                    binding.tvResponseText.setTextColor(Color.parseColor("#EF4444"))

                    // 응답 시간 표시
                    response.timestamp?.let { timestamp ->
                        binding.tvResponseTime.text = formatTime(timestamp)
                        binding.tvResponseTime.visibility = View.VISIBLE
                    } ?: run {
                        binding.tvResponseTime.visibility = View.GONE
                    }
                }

                ResponseType.QUIET -> {
                    binding.ivResponseIcon.setImageResource(R.drawable.ic_x_circle)
                    binding.ivResponseIcon.setColorFilter(Color.parseColor("#9CA3AF"))
                    binding.tvResponseText.text = "아니요, 조용해요"
                    binding.tvResponseText.setTextColor(Color.parseColor("#9CA3AF"))

                    response.timestamp?.let { timestamp ->
                        binding.tvResponseTime.text = formatTime(timestamp)
                        binding.tvResponseTime.visibility = View.VISIBLE
                    } ?: run {
                        binding.tvResponseTime.visibility = View.GONE
                    }
                }

                ResponseType.SORRY -> {
                    binding.ivResponseIcon.setImageResource(R.drawable.ic_alert_triangle)
                    binding.ivResponseIcon.setColorFilter(Color.parseColor("#F59E0B"))
                    binding.tvResponseText.text = "제가 주의할게요"
                    binding.tvResponseText.setTextColor(Color.parseColor("#F59E0B"))

                    response.timestamp?.let { timestamp ->
                        binding.tvResponseTime.text = formatTime(timestamp)
                        binding.tvResponseTime.visibility = View.VISIBLE
                    } ?: run {
                        binding.tvResponseTime.visibility = View.GONE
                    }
                }

                ResponseType.PENDING -> {
                    binding.ivResponseIcon.setImageResource(R.drawable.ic_clock)
                    binding.ivResponseIcon.setColorFilter(Color.parseColor("#D1D5DB"))
                    binding.tvResponseText.text = "응답 대기 중..."
                    binding.tvResponseText.setTextColor(Color.parseColor("#D1D5DB"))

                    binding.tvResponseTime.visibility = View.GONE
                }
            }
        }

        // 시간을 포맷팅하는 헬퍼 함수
        private fun formatTime(date: Date): String {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            return formatter.format(date)
        }
    }

    // 리스트 변경사항을 효율적으로 처리하기 위한 DiffCallback 클래스
    class ResponseDiffCallback : DiffUtil.ItemCallback<NeighborResponse>() {

        override fun areItemsTheSame(oldItem: NeighborResponse, newItem: NeighborResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NeighborResponse, newItem: NeighborResponse): Boolean {
            return oldItem == newItem
        }
    }

    // 어댑터에 새로운 응답 목록을 업데이트하는 함수
    fun updateResponses(responses: List<NeighborResponse>) {
        submitList(responses.toList())
    }
}
