package com.kau.ttokttok.ui.step2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kau.ttokttok.R
import com.kau.ttokttok.domain.model.NeighborResponse
import java.util.*

/**
 * 이웃들의 응답을 표시하는 RecyclerView 어댑터
 * 각 이웃의 위치, 응답 상태, 응답 시간을 보여줌
 */
class ResponseAdapter : ListAdapter<NeighborResponse, ResponseAdapter.ResponseViewHolder>(ResponseDiffCallback()) {

    /**
     * ViewHolder를 생성하는 함수
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder {
        val view = LayoutInflater.from(parent.context)// item_response.xml 레이아웃을 사용하여 ViewHolder 생성
            .inflate(R.layout.item_response, parent, false)
        return ResponseViewHolder(view)
    }

    /**
     * ViewHolder에 데이터를 바인딩하는 함수
     */
    override fun onBindViewHolder(holder: ResponseViewHolder, position: Int) {
        val response = getItem(position) // 현재 위치의 응답 데이터를 ViewHolder에 바인딩
        holder.bind(response)
    }

}