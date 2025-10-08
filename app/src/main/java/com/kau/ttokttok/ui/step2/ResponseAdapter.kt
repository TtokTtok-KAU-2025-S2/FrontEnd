package com.kau.ttokttok.ui.step2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kau.ttokttok.databinding.ItemResponseBinding
import com.kau.ttokttok.domain.model.step2.NeighborResponse

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
            // 기본 위치 정보만 바인딩
            binding.tvLocation.text = response.location
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
