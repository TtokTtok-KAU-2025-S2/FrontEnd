package com.kau.ttokttok.data.remote.dto.res

data class CommonRes<T>(
    val code: Int,
    val message: String,
    val result: T?
)
