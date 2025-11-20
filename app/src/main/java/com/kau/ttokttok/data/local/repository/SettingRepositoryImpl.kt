package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.domain.repository.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor() : SettingRepository {
    override suspend fun getNotice(): NetworkResult<String> {
        val Notice = """
            안녕하세요, 똑똑입니다.
            
            서비스 안정화를 위해 아래와 같이 시스템 점검을 진행할 예정입니다.
            새벽 시간에는 서비스 이용이 원활하지 않을 수 있으니 양해 부탁드립니다.
            
            - 점검 일시: 2025년 12월 22일 (월) 02:00 ~ 04:00
            - 점검 내용: 서버 안정화 및 데이터베이스 최적화
            
            더 좋은 서비스로 보답하겠습니다.
            감사합니다.
            """.trimIndent()
            return NetworkResult.Success(Notice)
    }

    override suspend fun getTerms(): NetworkResult<String> {
        val Terms = """
            제1조 (목적)
            이 약관은 똑똑(이하 "회사")이 제공하는 똑똑 및 똑똑 관련 제반 서비스의 이용과 관련하여 회사와 회원과의 권리, 의무 및 책임사항, 기타 필요한 사항을 규정함을 목적으로 합니다.
            
             ... (이하 생략) ...
             """.trimIndent()
             return NetworkResult.Success(Terms)
    }
    override suspend fun getPrivacyPolicy(): NetworkResult<String> {
        val Policy = """
            '똑똑'은(는) 「개인정보 보호법」 제30조에 따라 정보주체의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 처리방침을 수립·공개합니다.            
            
             ... (이하 생략) ...
             """.trimIndent()
             return NetworkResult.Success(Policy)
    }
}