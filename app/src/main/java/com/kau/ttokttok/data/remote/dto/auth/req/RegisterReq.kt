package com.kau.ttokttok.data.remote.dto.auth.req

import com.squareup.moshi.Json

data class RegisterReq(
    val aptId: Long,
    val email: String,
    val password: String,
    @Json(name = "dong")
    val buildingNumber: Int,
    @Json(name = "hosu")
    val unitNumber: Int
)
