package com.kau.ttokttok.domain.model

// 개별 알림 데이터 (RecyclerView 아이템으로 표시됨)
data class NotificationItem(
    val id: Long,              // 고유 ID (읽음 처리 등에 사용)
    val title: String,         // 알림 제목
    val message: String,       // 알림 내용
    val timeAgo: String,       // 시간 표시 (예: "10분 전")
    var isRead: Boolean,       // 읽음 여부
    var isNew: Boolean,        // NEW 배지 표시 여부
    val iconType: String       // 아이콘 타입: "noise", "consent", "comments", "announcement"
)
