package com.kau.ttokttok.ui.step2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.data.remote.NoiseInquiryRepository
import com.kau.ttokttok.domain.model.step2.*
import com.kau.ttokttok.domain.model.step2.enums.ResponseType
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// TODO: 백엔드 연동 시 에러 처리 로직 추가 필요 (네트워크 에러, 타임아웃 등)
/**
 * 소음 탐색 ViewModel
 * - 카테고리 목록은 즉시 조회(getCategories)
 * - 선택/로딩/에러의 별도 상태 관리는 제거
 * - inquiryState만 유지해서 화면 전환을 간단히 처리
 */
class NoiseInquiryViewModel(
    private val repository: NoiseInquiryRepository
) : ViewModel() {

    // TODO: 백엔드 연동 시 서버 실시간 데이터 반영
    val inquiryState: StateFlow<NoiseInquiry?> = repository.inquiryState

    // TODO: 백엔드 연동 시 서버에서 카테고리 가져오는 동안 로딩 상태 관리 필요
    fun getCategories(): List<NoiseCategory> = repository.getNoiseCategories()

    // TODO: 백엔드 연동 시 서버 응답 에러 처리 추가
    fun startInquiry(category: NoiseCategory) {
        viewModelScope.launch {
            repository.startInquiry(category)
        }
    }

    // TODO: 백엔드 연동 시 서버에 완료 요청 전송 및 응답 처리
    fun completeInquiry() {
        repository.completeInquiry()
    }

    // TODO: 백엔드 연동 시 서버에서 집계된 결과 가져오기
    fun getResult(): InquiryResult = repository.getInquiryResult()

    fun getCurrentResponses(): List<NeighborResponse> = inquiryState.value?.responses ?: emptyList()

    fun getCompletedResponseCount(): Int = getCurrentResponses().count { it.response != ResponseType.PENDING }

    fun getTotalNeighborCount(): Int = getCurrentResponses().size

    fun resetInquiry() {
        repository.resetInquiry()
    }
}