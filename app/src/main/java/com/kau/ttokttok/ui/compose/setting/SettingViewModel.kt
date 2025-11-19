package com.kau.ttokttok.ui.compose.setting

import androidx.lifecycle.ViewModel
import com.kau.ttokttok._core.network.auth.UserProvider
import com.kau.ttokttok.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.internal.throwMissingFieldException
import javax.inject.Inject

/**
 * Setting 화면의 전체 상태를 담는 데이터 클래스입니다.
 *
 * - ViewModel은 SettingUiState의 값을 변경하여 화면 상태를 업데이트하고
 * - SettingScreen은 이 상태를 관찰하면서 UI를 그립니다.
 *
 * ⚠️ Android 초보가 기억할 것:
 *   → Compose에서는 화면을 직접 수정하지 않고,
 *     이렇게 '상태(State)'를 바꾸면 UI가 자동으로 재렌더링됩니다.
 */

data class SettingUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val canNotifyAll: Boolean = true,
    val canNotifyNoiseVote: Boolean = true,
    val canNotifyPreConsideration: Boolean = true,
    val canNotifyCommunity: Boolean = true,

    val user: User? = null
)

// TODO: EVENT 정의
// Event에 맞게 UI를 다시 만들어라
sealed interface SettingEvent {
    data object NavigateToEditProfile : SettingEvent            // 프로필 수정 화면으로 이동
    data object NavigateToChangeNickname : SettingEvent         // 닉네임 변경 화면으로 이동
    data object NavigateToChangeAddress : SettingEvent          // 거주지 변경 화면으로 이동
    data object NavigateToChangePassword : SettingEvent         // 비밀번호 변경 화면으로 이동
    data object ShowNoticeDialog : SettingEvent              // 공지사항 화면을호 이동
    data object ShowTermsDialog : SettingEvent                  // 서비스 이용약관 화면으로 이동
    data object ShowPrivacyDialog : SettingEvent                // 개인정보 처리방침 화면으로 이동
    data object ShowLogoutConfirmDialog : SettingEvent          // 로그아웃 다이얼로그
    data object ShowDeleteAccountConfirmDialog : SettingEvent   // 탈퇴 다이얼로그
}

// TODO: UiAction 정의
// 사용자가 이러한 행동을 했다는 것을 알려준다.
sealed interface SettingUiAction {
    data object OnEditProfileClicked : SettingUiAction          // 가장 상단 수정 버튼 클릭
    //계정 관리
    data object OnChangeNicknameClicked : SettingUiAction       // 닉네임 변경 클릭
    data object OnChangeAddressClicked : SettingUiAction        // 거주지 변경 클릭
    data object OnChangePasswordClicked : SettingUiAction       // 비밀번호 변경 클릭
    data object OnLogoutClicked : SettingUiAction               // 로그아웃 클릭
    data object OnDeleteAccountClicked : SettingUiAction        // 회원 탈퇴 클릭
    // 앱 정보
    data object OnNoticeClicked : SettingUiAction               // 공지사항 클릭
    data object OnTermsClicked : SettingUiAction                // 서비스 이용약관 클릭
    data object OnPrivacyClicked : SettingUiAction              // 개인정보 처리방침 클릭
    //알림 설정 스위치(토글) 변경
    data class OnToggleNotifyAll(val checked: Boolean) : SettingUiAction
    data class OnToggleNotifyNoiseVote(val checked: Boolean) : SettingUiAction          //소음 확인
    data class OnToggleNotifyPreConsideration(val checked: Boolean) : SettingUiAction   // 사전양해 알림
    data class OnToggleNotifyCommunity(val checked: Boolean) : SettingUiAction          // 공지사항 알림
}

@HiltViewModel
class SettingViewModel @Inject constructor(
// BE연동시 repository 만들고 주입 후 사용
// private val repository: SettingRepository,
    private val userProvider: UserProvider
): ViewModel() {

    // 화면 상태를 보관하는 StateFlow
    // collectAsState()를 통해 Compose 화면에서 관찰한다.
    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState

    // 버튼 클릭 같은 단발성 이벤트를 담는 SharedFlow
    private val _events = MutableSharedFlow<SettingEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<SettingEvent> = _events.asSharedFlow()

    /**
     * 화면이 켜질 때 실행되는 초기 로딩 함수.
     *
     * 1) 로딩 시작
     * 2) Provider(캐시)에서 사용자/설정 정보를 가져옴
     * 3) UIState에 저장
     * 4) 로딩 종료
     */
    fun getSettingUi() {
        // 1) 로딩 시작
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        // 2) 데이터 로딩
        val newUser = getUserInformationToProvider()
        val information = getSettingInformationToProvider()

        // 3) UI 상태 업데이트
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = null,
            user = newUser
        )
    }

    // TODO: 각 이벤트에 맞게 부를 viewModel 메소드 작성

    // TODO: 각 액션에 맞게 부를 메소드 작성
    fun onUiAction(action: SettingUiAction) {
        when (action) {
            is SettingUiAction.OnToggleNotifyAll -> toggleNotifyAll(action.checked)
            is SettingUiAction.OnToggleNotifyNoiseVote -> _uiState.value = _uiState.value.copy(canNotifyNoiseVote = action.checked)
            is SettingUiAction.OnToggleNotifyPreConsideration -> _uiState.value = _uiState.value.copy(canNotifyPreConsideration = action.checked)
            is SettingUiAction.OnToggleNotifyCommunity -> _uiState.value = _uiState.value.copy(canNotifyCommunity = action.checked)

            SettingUiAction.OnEditProfileClicked -> emit(SettingEvent.NavigateToEditProfile)
            SettingUiAction.OnChangeNicknameClicked -> emit(SettingEvent.NavigateToChangeNickname)
            SettingUiAction.OnChangeAddressClicked -> emit(SettingEvent.NavigateToChangeAddress)
            SettingUiAction.OnChangePasswordClicked -> emit(SettingEvent.NavigateToChangePassword)
            SettingUiAction.OnNoticeClicked -> emit(SettingEvent.ShowNoticeDialog)
            SettingUiAction.OnTermsClicked -> emit(SettingEvent.ShowTermsDialog)
            SettingUiAction.OnPrivacyClicked -> emit(SettingEvent.ShowPrivacyDialog)

            SettingUiAction.OnLogoutClicked -> emit(SettingEvent.ShowLogoutConfirmDialog)
            SettingUiAction.OnDeleteAccountClicked -> emit(SettingEvent.ShowDeleteAccountConfirmDialog)
            else -> {

            }
        }
    }

    private fun getUserInformationToProvider(): User {
        val buildingNumber = userProvider.getBuildingNumberOrNull() ?: 0
        val unitNumber = userProvider.getUnitNumberOrNull() ?: 0

        return User("", buildingNumber, unitNumber)
    }

    private fun getSettingInformationToProvider() {
        // 추후 BE연동 시 SettingProvider.kt를 만들고, 캐시에 저장 된 값 꺼내 사용하기
    }

    // TODO: NoitifyAll을 바꾸면 -> 다른 toggle도 같이 꺼지도록 or 바꿀 수 없도록 한다.
    // TODO: NotifyAll이 false -> 다른 toggle의 값이 바뀌지 않도록 한다.
    private fun toggleNotifyAll(checked: Boolean) {
        val state = _uiState.value
        if (state.canNotifyAll == checked) return

        _uiState.value = state.copy(
            canNotifyAll = checked,
            canNotifyNoiseVote = checked,
            canNotifyPreConsideration = checked,
            canNotifyCommunity = checked,
        )
    }

    private fun emit(event: SettingEvent) {
        _events.tryEmit(event)
    }
}