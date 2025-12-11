package com.kau.ttokttok.domain.model.board.information

import com.kau.ttokttok.domain.model.common.enum.NoiseType

data class NoiseInformation(
    val maxNoiseValue: Int,
    val minNoiseValue: Int,
    val noiseType: NoiseType
)