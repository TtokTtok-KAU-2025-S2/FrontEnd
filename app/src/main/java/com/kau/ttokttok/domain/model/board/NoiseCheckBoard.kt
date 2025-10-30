package com.kau.ttokttok.domain.model.board

import com.kau.ttokttok.domain.model.board.information.NoiseInformation
import com.kau.ttokttok.domain.model.board.information.Vote
import java.time.LocalDateTime

data class NoiseCheckBoard(
    override val buildingNumber: String,
    override val localDateTime: LocalDateTime,
    override val title: String,
    override val content: String,
    override val commentValue: Int,

    val noiseInformation: NoiseInformation,
    val vote: Vote,
    val comments: List<Comment>

) : BoardPost
