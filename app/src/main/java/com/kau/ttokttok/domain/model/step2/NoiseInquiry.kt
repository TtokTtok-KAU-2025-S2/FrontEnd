package com.kau.ttokttok.domain.model.step2

import com.kau.ttokttok.domain.model.step2.enums.InquiryStatus

// 소음 탐색 진행 상황을 저장하는 데이터 클래스
data class NoiseInquiry(
    val category: NoiseCategory,                     // 탐색 중인 소음 카테고리
    var status: InquiryStatus = InquiryStatus.CATEGORY_SELECTION, // 현재 단계 상태
    var responses: List<NeighborResponse> = emptyList()           // 이웃 응답 리스트
)

