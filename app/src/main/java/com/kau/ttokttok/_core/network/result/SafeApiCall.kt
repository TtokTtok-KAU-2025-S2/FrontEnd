package com.kau.ttokttok._core.network.result

import com.kau.ttokttok._core.network.model.ApiResponse

suspend inline fun <T> safeApiCall(
    crossinline call: suspend () -> ApiResponse<T>
): NetworkResult<T> {
    return try {
        val res = call()

        if (res.isSuccess && res.result != null) {
            NetworkResult.Success(res.result)
        }

        NetworkResult.Error(code = res.code, message = res.message)

    } catch (e: Exception) {
        NetworkResult.Error(message = e.message, exception = e)
    }
}