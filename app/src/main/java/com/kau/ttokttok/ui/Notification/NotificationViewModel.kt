package com.kau.ttokttok.ui.Notification

import androidx.lifecycle.ViewModel
import com.kau.ttokttok.data.local.repository.NotificationRepository
import com.kau.ttokttok.domain.model.NotificationSettings

// 알림 화면 ViewModel
// 화면 회전 시에도 데이터 유지됨

class NotificationViewModel : ViewModel() {

    // TODO: 백엔드 연동 시 Repository를 DI(Hilt)로 주입받도록 변경
    private val repo = NotificationRepository()  // 데이터 저장소

    var isAllTabSelected = true  // 현재 탭: true=전체, false=읽지않음
        private set

    val settings get() = repo.getSettings()  // 현재 알림 설정

    // 현재 선택된 탭의 알림 목록 반환
    // TODO: 백엔드 연동 시 서버에서 알림 목록 가져오기
    fun getCurrentNotifications() =
        if (isAllTabSelected) repo.getAllNotifications()
        else repo.getUnreadNotifications()

    fun getUnreadCount() = repo.getUnreadNotifications().size  // 읽지 않은 알림 개수

    fun selectTab(all: Boolean) {  // 탭 선택 (버튼 클릭 시)
        isAllTabSelected = all
    }

    // TODO: 백엔드 연동 시 서버에 읽음 처리 요청
    fun markAllAsRead() = repo.markAllRead()  // 모두 읽음 처리

    // TODO: 백엔드 연동 시 서버에 삭제 요청
    fun deleteAllNotifications() = repo.deleteAll()  // 모두 삭제

    // TODO: 백엔드 연동 시 서버에 개별 읽음 처리 요청
    fun markAsRead(id: Long) = repo.markRead(id)  // 특정 알림 읽음 처리

    // TODO: 백엔드 연동 시 서버에 설정 저장 요청
    fun updateSettings(newSettings: NotificationSettings) =  // 설정 업데이트
        repo.saveSettings(newSettings)

    fun setDoNotDisturbTime(start: String, end: String) {  // 방해금지 시간 설정
        updateSettings(settings.copy(doNotDisturbStart = start, doNotDisturbEnd = end))
    }
}
