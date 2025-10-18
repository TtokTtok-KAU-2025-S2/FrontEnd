package com.kau.ttokttok.domain.model

// 알림 수신 설정 (설정 화면에서 사용)
data class NotificationSettings(
    var allNotifications: Boolean = true,      // 전체 알림 수신 여부
    var noiseDetection: Boolean = true,        // 똑똑 공동 탐색 알림
    var priorConsent: Boolean = true,          // 사전 양해 알림
    var comments: Boolean = true,              // 게시판 댓글 알림
    var announcements: Boolean = true,         // 관리사무소 공지 알림
    var doNotDisturbStart: String = "22:00",   // 방해금지 시작 시간
    var doNotDisturbEnd: String = "07:00"      // 방해금지 종료 시간
)
