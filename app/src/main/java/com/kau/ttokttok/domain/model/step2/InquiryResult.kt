package com.kau.ttokttok.domain.model.step2

// TODO: 백엔드 연동 시 서버에서 제공하는 추가 통계 정보 필드 추가 가능 (예: 평균 응답 시간, 신뢰도 등)
// 탐색 결과 통계 데이터
data class InquiryResult(
    val heardCount: Int,      // "들려요" 응답 수
    val quietCount: Int,      // "조용해요" 응답 수
    val sorryCount: Int,      // "주의할게요" 응답 수
    val totalResponses: Int   // 총 응답 수
)
