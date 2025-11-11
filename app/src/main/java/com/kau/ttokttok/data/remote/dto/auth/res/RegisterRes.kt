package com.kau.ttokttok.data.remote.dto.auth.res

import com.squareup.moshi.Json

data class RegisterRes(
    val id: Long,
    val email: String,
    @Json(name = "dong")
    val buildingNumber: Int,
    @Json(name = "hosu")
    val unitNumber: Int,
    val role: String
)