package com.kau.ttokttok.ui.xml.notification

import androidx.lifecycle.ViewModel
import com.kau.ttokttok.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repo: NotificationRepository  // Hilt가 자동으로 주입
) : ViewModel() {

    var isAllTabSelected = true  // 현재 탭: true=전체, false=읽지않음
        private set

    // 현재 선택된 탭의 알림 목록 반환
    fun getCurrentNotifications() =
        if (isAllTabSelected) repo.getAllNotifications()
        else repo.getUnreadNotifications()

    fun selectTab(all: Boolean) {  // 탭 선택 (버튼 클릭 시)
        isAllTabSelected = all
    }

    // 모두 읽음 처리 (백엔드 연동 시 서버에 요청)
    fun markAllAsRead() = repo.markAllRead()

    // 모두 삭제 (백엔드 연동 시 서버에 요청)
    fun deleteAllNotifications() = repo.deleteAll()

    // 특정 알림 읽음 처리 (백엔드 연동 시 서버에 요청)
    fun markAsRead(id: Long) = repo.markRead(id)
}