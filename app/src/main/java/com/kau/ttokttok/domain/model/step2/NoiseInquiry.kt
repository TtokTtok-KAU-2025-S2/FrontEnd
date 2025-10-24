package com.kau.ttokttok.domain.model.step2

import com.kau.ttokttok.domain.model.step2.enums.InquiryStatus

// TODO: 백엔드 연동 시 서버에서 부여하는 inquiry_id 필드 추가 필요
// 소음 탐색 진행 상황을 저장하는 데이터 클래스
data class NoiseInquiry(
    val category: NoiseCategory,                     // 탐색 중인 소음 카테고리
    var status: InquiryStatus = InquiryStatus.CATEGORY_SELECTION, // 현재 단계 상태
    var responses: List<NeighborResponse> = emptyList()           // 이웃 응답 리스트
    // TODO: 백엔드 연동 시 필드 추가 예시: val inquiryId: String, val createdAt: Date, val expiresAt: Date
)
