package com.kau.ttokttok.data.remote.dto.ai.req

import com.squareup.moshi.Json

data class GetAICategoryReq(
    @Json(name = "recordId")
    val recordId: Long
)

