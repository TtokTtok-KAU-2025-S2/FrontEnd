package com.kau.ttokttok.data.remote

import com.kau.ttokttok.domain.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date
import kotlin.random.Random

/**
 * 소음 탐색 기능 관련 데이터를 관리하는 저장소 클래스
 * 서버 통신은 없고, 하드코딩 및 시뮬레이션으로 동작함
 */
class NoiseInquiryRepository {

    // 현재 탐색 상태를 저장하며 변경 시 UI로 전달
    private val _inquiryState = MutableStateFlow<NoiseInquiry?>(null)
    val inquiryState: StateFlow<NoiseInquiry?> get() = _inquiryState

    fun getNoiseCategories(): List<NoiseCategory> {
        return listOf(
            NoiseCategory("living", "생활소음", "ic_home", NoiseRange.SURROUNDING, "#3B82F6"),
            NoiseCategory("pet", "애완동물 소음", "ic_users", NoiseRange.SURROUNDING, "#F97316"),
            NoiseCategory("machine", "기계 소음", "ic_wrench", NoiseRange.SURROUNDING, "#6B7280"),
            NoiseCategory("plumbing", "배관/설비 소음", "ic_droplet", NoiseRange.SURROUNDING, "#06B6D4"),
            NoiseCategory("external", "외부환경 소음", "ic_wind", NoiseRange.BUILDING, "#10B981"),
            NoiseCategory("other", "기타", "ic_more_horizontal", NoiseRange.SURROUNDING, "#8B5CF6")
        )
    }

    /**
     * 새로운 소음 탐색 시작
     * 선택된 카테고리 기반으로 가상의 이웃 리스트 생성 후 탐색 진행 상태 업데이트
     */
    suspend fun startInquiry(category: NoiseCategory) {
        // 탐색 범위에 맞는 가상의 이웃 응답 리스트 생성
        val responses = generateDummyResponses(category.range)

        // 초기 탐색 상태 생성 (전송 중 상태)
        val inquiry = NoiseInquiry(
            category = category,
            status = InquiryStatus.SENDING,
            responses = responses
        )
        _inquiryState.value = inquiry

        // 전송 중인 것처럼 2초 딜레이
        delay(2000L)

        // 전송 완료, 응답 수집 단계로 상태 변경
        _inquiryState.value = inquiry.copy(status = InquiryStatus.RESPONSES)

        // 각 이웃 응답 결과 무작위로 시뮬레이션 시작
        simulateResponses()
    }

    /**
     * 가상의 이웃 응답 리스트 생성
     * 동 전체 또는 주변 8세대 기준으로 위치 이름 생성
     */
    private fun generateDummyResponses(range: NoiseRange): List<NeighborResponse> {
        if (range == NoiseRange.BUILDING) {
            // 동 전체 20세대 임의로 생성
            return (1..20).map { idx ->
                NeighborResponse(
                    id = "building-$idx",
                    location = "${(idx - 1) / 4 + 1}층 ${((idx - 1) % 4) + 1}호"
                )
            }
        } else {
            // 주변 8세대 위치 이름 미리 지정, 어디서 온 답변인지는 모르게 해야할 듯 함
            return listOf(
                "위층 직접", "위층 대각", "아래층 직접", "아래층 대각",
                "좌측 인접", "좌측 2칸", "우측 인접", "우측 2칸"
            ).mapIndexed { idx, loc ->
                NeighborResponse(
                    id = "neighbor_$idx",
                    location = loc
                )
            }
        }
    }

    /**
     * 응답 상태를 무작위로 변경하는 시뮬레이션 함수 (임의의 수치)
     * 한 명씩 0.5초 간격으로 응답 상태 저장하고 상태 업데이트
     */
    private suspend fun simulateResponses() {
        val inquiry = _inquiryState.value ?: return
        val currentResponses = inquiry.responses.toMutableList()
        val possibleResponses = listOf(ResponseType.HEARD, ResponseType.QUIET, ResponseType.SORRY)

        for (i in currentResponses.indices) {
            delay(500L)

            // 응답 70% 확률로 기록, 30%는 대기 유지
            if (Random.nextFloat() < 0.7f) {
                val randomResponse = possibleResponses.random()
                currentResponses[i] = currentResponses[i].copy(
                    response = randomResponse,
                    timestamp = Date()
                )
                // 상태 갱신 (응답 수신 상태로 변경)
                _inquiryState.value = inquiry.copy(responses = currentResponses.toList())
            }
        }
    }

    /**
     * 현재 응답 통계 계산해서 결과 객체 반환
     */
    fun getInquiryResult(): InquiryResult {
        val responses = _inquiryState.value?.responses ?: emptyList()

        val heard = responses.count { it.response == ResponseType.HEARD }
        val quiet = responses.count { it.response == ResponseType.QUIET }
        val sorry = responses.count { it.response == ResponseType.SORRY }
        val total = responses.size

        return InquiryResult(
            heardCount = heard,
            quietCount = quiet,
            sorryCount = sorry,
            totalResponses = total
        )
    }

    /**
     * 탐색 데이터 초기화 함수
     */
    fun resetInquiry() {
        _inquiryState.value = null
    }
}
