package com.kau.ttokttok.domain.repository

import com.kau.ttokttok.domain.model.NotificationItem

// 알림 데이터 저장소 인터페이스 - Clean Architecture의 Domain Layer
interface NotificationRepository {

    fun getAllNotifications(): List<NotificationItem>  // 전체 알림 목록

    fun getUnreadNotifications(): List<NotificationItem>  // 읽지 않은 알림만 필터링

    fun markAllRead()  // 모두 읽음 처리

    fun deleteAll()  // 전체 삭제

    fun markRead(id: Long)  // 특정 알림 읽음 처리
}

