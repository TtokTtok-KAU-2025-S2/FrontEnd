package com.kau.ttokttok.data.remote.dto.res

// TODO: BE연동_수정
data class CommonRes<T>(
    val code: Int,
    val message: String,
    val result: T?
)
