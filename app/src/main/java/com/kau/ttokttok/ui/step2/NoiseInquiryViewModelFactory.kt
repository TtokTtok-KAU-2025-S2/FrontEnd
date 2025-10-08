package com.kau.ttokttok.ui.step2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kau.ttokttok.data.remote.NoiseInquiryRepository

/**
 * NoiseInquiryViewModel을 생성하기 위한 Factory 클래스
 * Repository 의존성을 주입하여 ViewModel을 생성함
 */
class NoiseInquiryViewModelFactory(
    private val repository: NoiseInquiryRepository
) : ViewModelProvider.Factory {

    /**
     * ViewModel을 생성하는 함수
     * @param modelClass 생성할 ViewModel의 클래스
     * @return 생성된 ViewModel 인스턴스
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // NoiseInquiryViewModel 타입인지 확인
        if (modelClass.isAssignableFrom(NoiseInquiryViewModel::class.java)) {
            // Repository를 주입하여 ViewModel 생성
            return NoiseInquiryViewModel(repository) as T
        }
        // 지원하지 않는 ViewModel 타입인 경우 예외 발생
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}