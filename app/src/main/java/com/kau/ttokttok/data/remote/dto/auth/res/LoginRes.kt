package com.kau.ttokttok.data.remote.dto.auth.res

import com.squareup.moshi.Json

data class LoginRes(
    val userDetailDto: UserDetailDto,
    val accessToken: String,
    val refreshToken: String
)

data class UserDetailDto(
    val id: Long,
    val email: String,
    @Json(name = "dong")
    val buildingNumber: Int,
    @Json(name = "hosu")
    val unitNumber: Int,
    val role: String
)