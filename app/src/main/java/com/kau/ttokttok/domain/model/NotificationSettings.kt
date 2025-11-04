package com.kau.ttokttok.domain.model

// 알림 수신 설정 (설정 화면에서 사용)
data class NotificationSettings(
    var allNotifications: Boolean = true,      // 전체 알림 수신 여부
    var noiseStatus: Boolean = true,           // 소음 현황판 알림 (리포트 공지, 투표)
    var priorConsent: Boolean = true,          // 사전 양해 알림
    var announcements: Boolean = true,         // 공지사항 알림 (관리자 공지)
    var monthlyReport: Boolean = true,         // 월간 리포트 알림 (AI 생성 소음 리포트)
    var doNotDisturbStart: String = "22:00",   // 방해금지 시작 시간
    var doNotDisturbEnd: String = "07:00"      // 방해금지 종료 시간
)
