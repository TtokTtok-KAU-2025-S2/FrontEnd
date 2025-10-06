package com.kau.ttokttok.domain.model

import java.util.Date

// 소음 카테고리를 나타내는 데이터 클래스
data class NoiseCategory(
    val id: String,               // 고유 식별자, 내부 식별용
    val name: String,             // 화면에 보여질 이름
    val iconName: String,         // 아이콘 이름 (벡터 이미지 리소스명)
    val range: NoiseRange,        // 알림 범위 (주변세대 or 동전체)
    val colorHex: String          // UI 표시용 색상 코드 (16진수)
)

// 알림 범위를 구분하는 enum 클래스
enum class NoiseRange {
    SURROUNDING,      // 주변 세대 (ex. 상하좌우 8세대)
    BUILDING          // 동 전체 (ex. 20세대)
}

// 이웃의 응답 종류를 나타내는 enum 클래스
enum class ResponseType {
    HEARD,    // 네, 저도 들려요
    QUIET,    // 아니요, 조용해요
    SORRY,    // 제가 주의할게요
    PENDING   // 응답 대기 중
}

// 개별 이웃의 응답 정보를 담는 데이터 클래스
data class NeighborResponse(
    val id: String,               // 임의 부여 이웃 고유 ID
    val location: String,         // 이웃 위치 설명 (ex. "위층 직접")
    var response: ResponseType = ResponseType.PENDING,   // 응답 상태 (기본은 대기 중)
    var timestamp: Date? = null   // 응답 시각 (응답 시 업데이트)
)

// 탐색 진행 상태를 구분하는 enum
enum class InquiryStatus {
    CATEGORY_SELECTION,       // 카테고리 선택 화면
    SENDING,                 // 알림 전송 중
    RESPONSES,               // 응답 수집 중
    RESULT                   // 결과 화면
}

// 소음 탐색 진행 상황을 저장하는 데이터 클래스
data class NoiseInquiry(
    val category: NoiseCategory,                     // 탐색 중인 소음 카테고리
    var status: InquiryStatus = InquiryStatus.CATEGORY_SELECTION, // 현재 단계 상태
    var responses: List<NeighborResponse> = emptyList()           // 이웃 응답 리스트
)

// 탐색 결과 통계 데이터
data class InquiryResult(
    val heardCount: Int,      // "들려요" 응답 수
    val quietCount: Int,      // "조용해요" 응답 수
    val sorryCount: Int,      // "주의할게요" 응답 수
    val totalResponses: Int   // 총 응답 수
)
