package com.kau.ttokttok.data.remote.repository

import com.kau.ttokttok.domain.model.NotificationItem
import com.kau.ttokttok.domain.repository.NotificationRepository
import javax.inject.Inject
import javax.inject.Singleton

// 알림 데이터 저장소 구현체 - Data Layer
@Singleton
class NotificationRepositoryImpl @Inject constructor() : NotificationRepository {

    // 테스트용 더미 데이터 (추후 서버에서 받아옴)
    private val notifications = mutableListOf(
        NotificationItem(1, "소음 현황판", "새로운 소음 리포트가 공지되었습니다", "10분 전", false, true, "noise_status"),
        NotificationItem(2, "사전 양해", "101동 주민이 사전 양해를 요청했습니다", "5시간 전", false, true, "prior_consent"),
        NotificationItem(3, "공지사항", "관리자가 새 공지사항을 등록했습니다", "1시간 전", true, false, "announcement"),
        NotificationItem(4, "월간 리포트", "11월 소음 분석 리포트가 생성되었습니다", "어제", true, false, "monthly_report")
    )

    override fun getAllNotifications(): List<NotificationItem> = notifications  // 전체 알림 목록

    override fun getUnreadNotifications(): List<NotificationItem> =  // 읽지 않은 알림만 필터링
        notifications.filter { !it.isRead }

    override fun markAllRead() {  // 모두 읽음 처리
        notifications.forEach { it.isRead = true; it.isNew = false }
    }

    override fun deleteAll() = notifications.clear()  // 전체 삭제

    override fun markRead(id: Long) {  // 특정 알림 읽음 처리
        notifications.find { it.id == id }?.apply { isRead = true; isNew = false }
    }
}
