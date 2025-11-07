package com.kau.ttokttok._core.network.result

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(
        val code: String? = null,
        val message: String? = null,
        val exception: Throwable? = null
    ) : NetworkResult<Nothing>()
}