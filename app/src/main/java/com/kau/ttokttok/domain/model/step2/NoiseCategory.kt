package com.kau.ttokttok.domain.model.step2

import com.kau.ttokttok.domain.model.step2.enums.NoiseRange

// TODO: 백엔드 연동 시 서버에서 제공하는 카테고리 데이터 구조에 맞춰 수정 필요
// 소음 카테고리를 나타내는 데이터 클래스
data class NoiseCategory(
    val id: String,               // 고유 식별자 (TODO: 서버 DB의 category_id와 매핑)
    val name: String,             // 화면에 보여질 이름
    val iconName: String,         // 아이콘 이름 (TODO: 서버에서 아이콘 URL 제공 시 수정)
    val range: NoiseRange,        // 알림 범위
    val colorHex: String          // UI 표시용 색상 코드
)
