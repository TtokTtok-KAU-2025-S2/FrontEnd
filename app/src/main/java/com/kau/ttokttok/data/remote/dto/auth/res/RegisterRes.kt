package com.kau.ttokttok.data.remote.dto.auth.res

import com.squareup.moshi.Json

data class RegisterRes(
    // TODO: BE연동
    val id: Long,
    val email: String,
    @Json(name = "dong")
    val buildingNumber: Int,
    @Json(name = "hosu")
    val unitNumber: Int,
    val role: String,
    val accessToken: String,
    val refreshToken: String,
)