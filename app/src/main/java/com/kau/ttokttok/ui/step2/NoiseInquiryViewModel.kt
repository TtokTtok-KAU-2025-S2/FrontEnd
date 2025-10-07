package com.kau.ttokttok.ui.step2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok.data.remote.NoiseInquiryRepository
import com.kau.ttokttok.domain.model.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 소음 탐색 ViewModel
 * - 카테고리 목록은 즉시 조회(getCategories)
 * - 선택/로딩/에러의 별도 상태 관리는 제거
 * - inquiryState만 유지해서 화면 전환을 간단히 처리
 */
class NoiseInquiryViewModel(
    private val repository: NoiseInquiryRepository
) : ViewModel() {

    // 레포지토리의 현재 탐색 상태(카테고리/단계/응답 목록)가 들어있음
    val inquiryState: StateFlow<NoiseInquiry?> = repository.inquiryState

    // 카테고리 목록을 즉시 반환
    fun getCategories(): List<NoiseCategory> = repository.getNoiseCategories()

    // 선택한 카테고리로 탐색 시작
    fun startInquiry(category: NoiseCategory) {
        viewModelScope.launch {
            repository.startInquiry(category)
        }
    }

    //응답 수집 종료 → 결과 화면으로 전환
    fun completeInquiry() {
        repository.completeInquiry()
    }

    // 현재 응답들을 바탕으로 결과 계산
    fun getResult(): InquiryResult = repository.getInquiryResult()

    // 현재 응답 리스트
    fun getCurrentResponses(): List<NeighborResponse> = inquiryState.value?.responses ?: emptyList()

    // 응답 완료된 이웃 수
    fun getCompletedResponseCount(): Int = getCurrentResponses().count { it.response != ResponseType.PENDING }

    // 전체 이웃 수
    fun getTotalNeighborCount(): Int = getCurrentResponses().size
}