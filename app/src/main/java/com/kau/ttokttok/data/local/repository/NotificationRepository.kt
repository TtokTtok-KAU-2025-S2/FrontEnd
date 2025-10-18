package com.kau.ttokttok.data.local.repository

import com.kau.ttokttok.domain.model.NotificationItem
import com.kau.ttokttok.domain.model.NotificationSettings

// 알림 데이터 저장소 - ViewModel과 데이터 사이의 중간 계층
class NotificationRepository {

    // 테스트용 더미 데이터 (추후 서버에서 받아옴)
    private val notifications = mutableListOf(
        NotificationItem(1, "똑똑 공동 탐색 응답", "소음 탐지 알림", "10분 전", false, true, "noise"),
        NotificationItem(2, "사전 양해", "사전 양해 알림", "5시간 전", false, true, "consent"),
        NotificationItem(3, "새 댓글 알림", "댓글 알림", "1시간 전", true, false, "comments"),
        NotificationItem(4, "관리사무소 공지", "중요 공지사항", "어제", true, false, "announcement")
    )

    private var settings = NotificationSettings()  // 알림 설정 (추후 DataStore에 저장)

    fun getAllNotifications(): List<NotificationItem> = notifications  // 전체 알림 목록

    fun getUnreadNotifications(): List<NotificationItem> =  // 읽지 않은 알림만 필터링
        notifications.filter { !it.isRead }

    fun markAllRead() {  // 모두 읽음 처리
        notifications.forEach { it.isRead = true; it.isNew = false }
    }

    fun deleteAll() = notifications.clear()  // 전체 삭제

    fun markRead(id: Long) {  // 특정 알림 읽음 처리
        notifications.find { it.id == id }?.apply { isRead = true; isNew = false }
    }

    fun getSettings(): NotificationSettings = settings  // 현재 설정 가져오기

    fun saveSettings(newSettings: NotificationSettings) {  // 설정 저장
        settings = newSettings
    }
}
