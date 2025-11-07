package com.kau.ttokttok.data.remote.dto.auth.req

// TODO: 로그인/회원가입 페이지_팀원들과 회의 후 교체
data class RegisterReq(
    val email: String,
    val password: String,
    val buildingNumber: String,
    val unitNumber: String
)
