package com.kau.ttokttok.domain.model.step2

import com.kau.ttokttok.domain.model.step2.enums.ResponseType
import java.util.Date

// 개별 이웃의 응답 정보를 담는 데이터 클래스
data class NeighborResponse(
    val id: String,               // 임의 부여 이웃 고유 ID
    val location: String,         // 이웃 위치 설명 (ex. "위층 직접")
    var response: ResponseType = ResponseType.PENDING,   // 응답 상태 (기본은 대기 중)
    var timestamp: Date? = null   // 응답 시각 (응답 시 업데이트)
)

