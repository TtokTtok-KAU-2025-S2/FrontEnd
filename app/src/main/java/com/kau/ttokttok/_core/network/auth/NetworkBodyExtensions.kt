package com.kau.ttokttok._core.network.auth

// RequestBodyExtensions.kt
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

private val TEXT_PLAIN = "text/plain".toMediaType()

fun String.toPlainRequestBody(): RequestBody =
    this.toRequestBody(TEXT_PLAIN)
