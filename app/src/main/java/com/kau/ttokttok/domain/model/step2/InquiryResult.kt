package com.kau.ttokttok.domain.model.step2

// 탐색 결과 통계 데이터
data class InquiryResult(
    val heardCount: Int,      // "들려요" 응답 수
    val quietCount: Int,      // "조용해요" 응답 수
    val sorryCount: Int,      // "주의할게요" 응답 수
    val totalResponses: Int   // 총 응답 수
)

