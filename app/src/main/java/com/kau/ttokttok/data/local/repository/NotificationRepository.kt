package com.example.noisepreventionapp.data

import com.example.noisepreventionapp.model.NotificationItem
import com.example.noisepreventionapp.model.NotificationSettings

class NotificationRepository {

    private val notifications = mutableListOf(
        NotificationItem(1, "똑똑 공동 탐색 응답", "소음 탐지 알림", "10분 전", false, true, "noise"),
        NotificationItem(2, "사전 양해", "사전 양해 알림", "5시간 전", false, true, "consent"),
        NotificationItem(3, "새 댓글 알림", "댓글 알림", "1시간 전", true, false, "comments"),
        NotificationItem(4, "관리사무소 공지", "중요 공지사항", "어제", true, false, "announcement")
    )

    private var settings = NotificationSettings()

    fun getAllNotifications(): List<NotificationItem> = notifications

    fun getUnreadNotifications(): List<NotificationItem> =
        notifications.filter { !it.isRead }

    fun markAllRead() {
        notifications.forEach { it.isRead = true; it.isNew = false }
    }

    fun deleteAll() = notifications.clear()

    fun markRead(id: Long) {
        notifications.find { it.id == id }?.apply { isRead = true; isNew = false }
    }

    fun getSettings(): NotificationSettings = settings

    fun saveSettings(newSettings: NotificationSettings) {
        settings = newSettings
    }
}
