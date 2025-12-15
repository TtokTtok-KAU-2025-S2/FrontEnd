package com.kau.ttokttok.domain.model.common.enum

enum class NoiseType(val label: String) {
    FOOTSTEPS("발걸음 소리"),
    HAMMERING("망치질 소리"),
    FURNITURE("가구 끄는 소리"),
    MUSIC("음악 소리"),
    VOICE("고성방가"),
    PET("반려동물 소리"),
    APPLIANCE("가전제품 소리"),
    DOOR("문 여닫는 소리"),
    WATER("물 소리"),
    CONSTRUCTION("인테리어 공사"),
    EXERCISE("운동 기구 소리"),
    UNKNOWN("기타");

    override fun toString(): String = name
}

fun String.toNoiseType(): NoiseType =
    NoiseType.entries.find{ it.name.equals(this, ignoreCase = true)} ?: NoiseType.UNKNOWN
