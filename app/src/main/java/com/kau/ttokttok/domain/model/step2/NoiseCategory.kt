package com.kau.ttokttok.domain.model.step2

import com.kau.ttokttok.domain.model.step2.enums.NoiseRange

// 소음 카테고리를 나타내는 데이터 클래스
data class NoiseCategory(
    val id: String,               // 고유 식별자, 내부 식별용
    val name: String,             // 화면에 보여질 이름
    val iconName: String,         // 아이콘 이름 (벡터 이미지 리소스명)
    val range: NoiseRange,        // 알림 범위 (주변세대 or 동전체)
    val colorHex: String          // UI 표시용 색상 코드 (16진수)
)

