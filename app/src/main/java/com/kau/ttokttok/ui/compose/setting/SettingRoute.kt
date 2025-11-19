package com.kau.ttokttok.ui.compose.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kau.ttokttok.ui.component.common.AppDialog
import com.kau.ttokttok.ui.navigation.Destination

@Composable
fun SettingRoute(
    // 해당 SettingViewModel에 맞는 viewModel을 불러온다. (ex. Spring 컨테이너에 등록된 Bean을 자동으로 주입 (Autowired))
    viewModel: SettingViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNavigate: (Destination) -> Unit,
    onResetToLogin: () -> Unit,
) {
    // viewModel의 uiState를 관찰 중
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // 다이얼로그 표시 상태
    var showNoticeDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }


    // 해당 화면을 실행할 때 실행한다. (ex. 클래스를 생성하면 생성자 로직이 진행되는 것과 유사하다)
    LaunchedEffect(viewModel) {
        viewModel.getSettingUi()

        // viewModel의 event를 관찰하고, 그에 맞게 event에 맞게 상호작용을 진행한다.
        viewModel.events.collect { event ->
            // TODO: 각 이벤트에 맞게 추가해야 하는 내용들 정리
            when (event) {
                SettingEvent.NavigateToEditProfile -> {
                    onNavigate(Destination.EDIT_PROFILE)
                }
                SettingEvent.NavigateToChangeNickname -> {
                    onNavigate(Destination.CHANGE_NICKNAME)
                }
                SettingEvent.NavigateToChangeAddress -> {
                    onNavigate(Destination.CHANGE_ADDRESS)
                }
                SettingEvent.NavigateToChangePassword -> {
                    onNavigate(Destination.CHANGE_PASSWORD)
                }
                SettingEvent.ShowNoticeDialog -> {
                    showNoticeDialog = true
                }
                SettingEvent.ShowTermsDialog -> {
                    showTermsDialog = true
                }
                SettingEvent.ShowPrivacyDialog -> {
                    showPrivacyDialog = true
                }
                SettingEvent.ShowLogoutConfirmDialog -> {
                    showLogoutDialog = true
                }
                SettingEvent.ShowDeleteAccountConfirmDialog -> {
                    showDeleteAccountDialog = true
                }
            }
        }
    }

    // 해당 값들을 토대로 값을 불러온다.
    SettingScreen(
        uiState = uiState,
        onEvent = viewModel::onUiAction,
        onBack = onBack,
    )
    // 공지사항 다이얼로그
    if (showNoticeDialog) {
        AppDialog(
            title = "앱 공지사항",
            message = "공지사항 내용",
            onDismiss = { showNoticeDialog = false }
        )
    }
    // 서비스 이용약관 다이얼로그
    if (showTermsDialog) {
        AppDialog(
            title = "서비스 이용약관",
            message = "이용 약관 내용",
            onDismiss = { showTermsDialog = false }
        )
    }
    // 개인정보 처리방침 다이얼로그
    if (showPrivacyDialog) {
        AppDialog(
            title = "개인정보 처리방침",
            message = "개인정보 처리방침 내용",
            onDismiss = { showPrivacyDialog = false }
        )
    }
    // 로그아웃 확인 다이얼로그
    if (showLogoutDialog) {
        AppDialog(
            title = "로그아웃",
            message = "정말 로그아웃 하시겠습니까?",
            onDismiss = { showLogoutDialog = false },
            dismissText = "취소",
            onConfirm = {
                showLogoutDialog = false
                onResetToLogin()
            },
            confirmText = "로그아웃 완료"
        )
    }
    // 회원 탈퇴 확인 다이얼로그
    if (showDeleteAccountDialog) {
        AppDialog(
            title = "회원 탈퇴",
            message = "정말 회원탈퇴 하시겠습니까?",
            onDismiss = { showDeleteAccountDialog = false },
            dismissText = "취소",
            onConfirm = {
                showDeleteAccountDialog = false
                onResetToLogin()
            },
            confirmText = "탈퇴 완료"
        )
    }
}