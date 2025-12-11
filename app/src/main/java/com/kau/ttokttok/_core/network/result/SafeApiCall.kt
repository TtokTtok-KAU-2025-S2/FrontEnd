package com.kau.ttokttok._core.network.result

import com.kau.ttokttok._core.network.model.ApiResponse
import kotlinx.coroutines.CancellationException
import org.json.JSONObject
import retrofit2.HttpException

suspend inline fun <T> safeApiCall(
    crossinline call: suspend () -> ApiResponse<T>
): NetworkResult<T> {
    return try {
        val res = call()

        if (res.isSuccess && res.result != null) {
            return NetworkResult.Success(res.result)
        }

        NetworkResult.Error(
            isSuccess = res.isSuccess,
            code = res.code,
            message = res.message
        )

    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        val backendMessage = extractMessageFromJson(errorBody)
        val backendCode = extractCodeFromJson(errorBody)

        NetworkResult.Error(
            isSuccess = false,
            code = backendCode ?: e.code().toString(),
            message = backendMessage ?: "요청 처리 중 오류가 발생했습니다."
        )

    } catch (e: CancellationException) {
        throw e

    } catch (e: Exception) {
        NetworkResult.Error(
            isSuccess = false,
            code = null,
            message = e.message ?: "네트워크 오류가 발생했습니다."
        )
    }
}

fun extractMessageFromJson(raw: String?): String? {
    return try {
        if (raw.isNullOrBlank()) return null
        val json = JSONObject(raw)
        json.optString("message", null)
    } catch (_: Exception) {
        null
    }
}

fun extractCodeFromJson(raw: String?): String? {
    return try {
        if (raw.isNullOrBlank()) return null
        val json = JSONObject(raw)
        json.optString("code", null)
    } catch (_: Exception) {
        null
    }
}