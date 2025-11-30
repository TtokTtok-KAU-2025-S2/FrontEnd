package com.kau.ttokttok.domain.model.board.noisevote

enum class NoiseVoteType {
    HEARD,
    NOT_HEARD,
    BE_CAREFUL;

    companion object {
        fun from(value: String): NoiseVoteType =
            entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid vote type: $value")
    }
}