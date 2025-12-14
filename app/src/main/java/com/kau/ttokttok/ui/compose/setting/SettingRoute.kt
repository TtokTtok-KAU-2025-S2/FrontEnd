package com.kau.ttokttok.ui.compose.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kau.ttokttok.ui.component.common.AppDialog
import com.kau.ttokttok.ui.component.common.TtokTtokInputDialog
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
    var showChangeAddressConfirmDialog by remember {  mutableStateOf(false) }
    var showAddressChangeSuccessDialog by remember { mutableStateOf(false) }
    var showAddressChangeFailureDialog by remember { mutableStateOf(false) }
    var addressChangeFailureMessage by remember { mutableStateOf("") }
    var showChangePasswordConfirmDialog by remember {  mutableStateOf(false) }
    var showPasswordChangeSuccessDialog by remember { mutableStateOf(false) }
    var showPasswordChangeFailureDialog by remember { mutableStateOf(false) }
    var passwordChangeFailureMessage by remember { mutableStateOf("") }     // 실패 메시지 저장할 변수
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
                SettingEvent.ShowChangeAddressConfirmDialog -> {
                    showChangeAddressConfirmDialog = true
                }
                SettingEvent.ShowAddressChangeSuccessDialog -> {
                    showAddressChangeSuccessDialog = true
                }
                is SettingEvent.ShowAddressChangeFailureDialog -> {
                    addressChangeFailureMessage = event.message
                    showAddressChangeFailureDialog = true
                }
                SettingEvent.ShowChangePasswordConfirmDialog -> {
                    showChangePasswordConfirmDialog = true
                }
                SettingEvent.ShowPasswordChangeSuccessDialog -> {
                    showPasswordChangeSuccessDialog = true
                }
                is SettingEvent.ShowPasswordChangeFailureDialog -> {
                    passwordChangeFailureMessage = event.message
                    showPasswordChangeFailureDialog = true
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
        onConfirmLogout = viewModel::performLogout
    )
    // 거주지변경 다이얼로그
    if (showChangeAddressConfirmDialog) {
        TtokTtokInputDialog(
            title = "거주지 변경",
            onDismiss = { showChangeAddressConfirmDialog = false },
            onConfirm = { viewModel.onUiAction(SettingUiAction.OnChangeAddressConfirmClicked) },
            confirmText = "변경"
        ) {
            Column {
                OutlinedTextField(
                    value = uiState.newAddressInput,
                    onValueChange = {
                        viewModel.onUiAction(
                            SettingUiAction.OnNewAddressInputChanged(
                                it
                            )
                        )
                    },
                    label = { Text("새 거주지") },
                    modifier = Modifier.fillMaxWidth()
                )

                uiState.addressChangeError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
    // 비밀번호 변경 다이얼로그
    if (showChangePasswordConfirmDialog) {
        TtokTtokInputDialog(
            title = "비밀번호 변경",
            onDismiss = { showChangePasswordConfirmDialog = false },
            dismissText = "취소",
            onConfirm = { viewModel.onUiAction(SettingUiAction.OnChangePasswordConfirmClicked) },
            confirmText = "변경"
        ) {
            Column {
                OutlinedTextField(
                    value = uiState.newPasswordInput,
                    onValueChange = { viewModel.onUiAction(SettingUiAction.OnNewPasswordInputChanged(it)) },
                    label = { Text("새 비밀번호") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.newPasswordConfirmInput,
                    onValueChange = { viewModel.onUiAction(SettingUiAction.OnNewPasswordConfirmInputChanged(it)) },
                    label = { Text("새 비밀번호 확인") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                uiState.passwordChangeError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
    // 공지사항 다이얼로그
    if (showNoticeDialog) {
        AppDialog(
            title = "앱 공지사항",
            message = uiState.noticeContent,
            onDismiss = { showNoticeDialog = false }
        )
    }
    // 서비스 이용약관 다이얼로그
    if (showTermsDialog) {
        AppDialog(
            title = "서비스 이용약관",
            message = uiState.termsContent,
            onDismiss = { showTermsDialog = false }
        )
    }
    // 개인정보 처리방침 다이얼로그
    if (showPrivacyDialog) {
        AppDialog(
            title = "개인정보 처리방침",
            message = uiState.privacyPolicyContent,
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
                viewModel.performLogout()
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
                viewModel.performLogout()
                showDeleteAccountDialog = false
                onResetToLogin()
            },
            confirmText = "탈퇴 완료"
        )
    }
    // 거주지 변경 성공 다이얼로그
    if (showAddressChangeSuccessDialog) {
        AppDialog(
            title = "거주지 변경",
            message = "거주지가 성공적으로 변경되었습니다.",
            onDismiss = { showAddressChangeSuccessDialog = false },
            onConfirm = { showAddressChangeSuccessDialog = false },
            confirmText = "확인"
        )
    }

    // 거주지 변경 실패 다이얼로그
    if (showAddressChangeFailureDialog) {
        AppDialog(
            title = "거주지 변경 실패",
            message = addressChangeFailureMessage,
            onDismiss = { showAddressChangeFailureDialog = false },
            onConfirm = { showAddressChangeFailureDialog = false },
            confirmText = "확인"
        )
    }

    // 비밀번호 변경 성공 다이얼로그
    if (showPasswordChangeSuccessDialog) {
        AppDialog(
            title = "비밀번호 변경",
            message = "비밀번호가 성공적으로 변경되었습니다.",
            onDismiss = { showPasswordChangeSuccessDialog = false },
            onConfirm = { showPasswordChangeSuccessDialog = false },
            confirmText = "확인"
        )
    }
    // 비밀번호 변경 실패 다이얼로그
    if (showPasswordChangeFailureDialog) {
        AppDialog(
            title = "비밀번호 변경 실패",
            message = passwordChangeFailureMessage,
            onDismiss = { showPasswordChangeFailureDialog = false },
            onConfirm = { showPasswordChangeFailureDialog = false },
            confirmText = "확인"
        )
    }
}