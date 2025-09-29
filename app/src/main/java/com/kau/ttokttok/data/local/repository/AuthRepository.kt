package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok.data.local.storage.AuthStorage
import com.kau.ttokttok.data.remote.api.AuthApiService
import com.kau.ttokttok.data.remote.dto.req.LoginReq
import com.kau.ttokttok.domain.model.User

class AuthRepository(
    private val api: AuthApiService,
    private val storage: AuthStorage
) {
    // 로그인

    // 회원가입

    // 로그아웃

}