package com.kau.ttokttok.domain.usecase.auth

data class LoginResult(
    val accessToken: String,
    val refreshToken: String,
    val buildingNumber: Int,
    val unitNumber: Int
)
