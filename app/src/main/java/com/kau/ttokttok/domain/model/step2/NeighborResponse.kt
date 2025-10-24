package com.kau.ttokttok.domain.model.step2

import com.kau.ttokttok.domain.model.step2.enums.ResponseType
import java.util.Date

// TODO: 백엔드 연동 시 서버 응답 JSON 구조에 맞춰 필드 추가/수정 필요 (예: userId, userName 등)
// 개별 이웃의 응답 정보를 담는 데이터 클래스
data class NeighborResponse(
    val id: String,               // 이웃 고유 ID (TODO: 백엔드에서 실제 user_id 사용)
    val location: String,         // 이웃 위치 설명 (TODO: 백엔드에서 익명화된 위치 정보 제공)
    var response: ResponseType = ResponseType.PENDING,   // 응답 상태
    var timestamp: Date? = null   // 응답 시각
)
