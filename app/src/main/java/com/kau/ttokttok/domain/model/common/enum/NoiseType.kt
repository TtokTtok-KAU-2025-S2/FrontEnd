package com.kau.ttokttok.domain.model.common.enum

enum class NoiseType(val label: String) {
    FOOTSTEP("발걸음 소리"),
    HAMMERING("망치질 소리"),
    FURNITURE("가구 끄는 소리"),
    MUSIC("음악 소리"),
    UNKNOWN("기타");

    override fun toString(): String = name
}

fun String.toNoiseType(): NoiseType =
    NoiseType.entries.find{ it.name.equals(this, ignoreCase = true)} ?: NoiseType.UNKNOWN