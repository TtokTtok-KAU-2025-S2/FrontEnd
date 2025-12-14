package com.kau.ttokttok.domain.model

import java.util.Date

/**
 * 소음 일기 모델
 * TODO: [백엔드 연동] 서버 응답 DTO와 매핑이 필요할 수 있습니다.
 * TODO: [백엔드 연동] 필요한 필드 추가 고려사항:
 * - userId: String? (사용자 식별)
 * - createdAt: Date? (서버에서 생성 시각)
 * - updatedAt: Date? (서버에서 수정 시각)
 * - audioFileUrl: String? (녹음 파일이 서버에 저장되는 경우)
 * - location: String? (소음 발생 위치 정보)
 * TODO: [백엔드 연동] toDto(), fromDto() 확장 함수 추가 필요
 */
data class NoiseLog(
    val id: String? = null, // TODO: [백엔드 연동] 서버에서 생성된 ID로 교체
    val noiseType: String, // "발걸음", "망치질" 등
    val maxDecibel: Double,
    val avgDecibel: Double,
    val memo: String,
    val measuredAt: Date,
    // 측정 소요 시간(초 단위). 측정 화면에서 전달된 duration을 서버 Create/Modify API에 넘길 때 사용.
    val duration: Long = 0L,
    val hasReport: Boolean = false // TODO: [백엔드 연동] 서버의 리포트 생성 상태와 동기화
)
