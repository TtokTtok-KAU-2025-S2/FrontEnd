package com.kau.ttokttok.data.remote.dto.report.res

data class GetApartmentStatsRes(
    val apartments: List<Apartment>,
    val totalApartments: Int
)

data class Apartment(
    val apartmentId: Long,
    val apartmentName: String,
    // status: 앱 사용량/활성화 여부 (HIGH=활발, MEDIUM=보통, LOW=저조)
    // TODO: 추후 Enum으로 변경
    val status: String,
    // 소음 유형별 분포 데이터
    // TODO: 추후 Enum으로 변경
    val noiseDistribution: NoiseDistribution
)

data class NoiseDistribution(
    val FOOTSTEPS: Int? = 0,
    val FURNITURE: Int? = 0,
    val HAMMERING: Int? = 0,
    val MUSIC: Int? = 0,
    val UNKNOWN: Int? = 0
)
