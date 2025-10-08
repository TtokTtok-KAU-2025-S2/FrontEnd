package com.kau.ttokttok.domain.model.step2.enums

// 탐색 진행 상태를 구분하는 enum
enum class InquiryStatus {
    CATEGORY_SELECTION,       // 카테고리 선택 화면
    SENDING,                 // 알림 전송 중
    RESPONSES,               // 응답 수집 중
    RESULT                   // 결과 화면
}

