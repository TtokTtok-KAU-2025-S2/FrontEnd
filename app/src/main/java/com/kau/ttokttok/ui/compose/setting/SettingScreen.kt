package com.kau.ttokttok.ui.compose.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kau.ttokttok.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Preview(heightDp = 2000)
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    uiState: SettingUiState = SettingUiState(),
    onEvent: (SettingUiAction) -> Unit = {},
    onBack: () -> Unit = {},
    onConfirmLogout: () -> Unit = {}
) {
    val scroll = rememberScrollState()

    // 전체 배경 그라데이션
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF0FDF4), // tailwind green-50 근사값
                        Color(0xFFEFF6FF)  // tailwind blue-50 근사값
                    )
                )
            )
    ) {
        Scaffold(
            // sticky top-0 역할
            topBar = {
                // Tailwind: bg-white/80 + backdrop-blur-sm + border-b
                // Compose엔 진짜 blur는 없으니 반투명 Surface + 하단 Divider로 유사 구현
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                    tonalElevation = 0.dp, // 유리 느낌 살리기 위해 0
                ) {
                    Column {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(
                                    onClick = onBack,
                                    modifier = Modifier.padding(start = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "뒤로가기"
                                    )
                                }
                            },
                            title = {
                                Column {
                                    Text(
                                        text = "설정",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = Color(0xFF1F2937) // gray-800
                                        )
                                    )
                                    Text(
                                        text = "계정 및 앱 설정",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF4B5563) // gray-600
                                        ),
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        )
                        Divider(color = Color(0xFFE5E7EB)) // gray-200/50 유사
                    }
                }
            },
            containerColor = Color.Transparent, // 배경 그라데이션이 보이도록
            contentColor = MaterialTheme.colorScheme.onBackground
        ) { innerPadding ->
            // 본문: max-w-lg + mx-auto + px-4 + py-4
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .verticalScroll(scroll),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // max-w-lg (약 32rem ≒ 512dp) 근사화
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 512.dp)
                ) {
                    // TODO: viewModel 연결
                    SettingCard(
                        content = {
                            ProfileCard(
                                onEditClick = {onEvent(SettingUiAction.OnEditProfileClicked)},
                                user = uiState.user ?: User("", 0, 0)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    SettingCard {
                        NotificationSettingsSection(
                            uiState = uiState,
                            onEvent = onEvent
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    // TODO: viewModel 연결
                    SettingCard {
                        AccountManagementSection(
                            onChangeAddress = {onEvent(SettingUiAction.OnChangeAddressClicked)},
                            onChangePassword = {onEvent(SettingUiAction.OnChangePasswordClicked)},
                            onLogout = {onEvent(SettingUiAction.OnLogoutClicked)},
                            onDeleteAccount = {onEvent(SettingUiAction.OnDeleteAccountClicked)}
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    // TODO: viewModel 연결
                    SettingCard {
                        AppInfoSection(
                            appVersion = "v1.0.0",
                            onNoticeClick = {onEvent(SettingUiAction.OnNoticeClicked)},
                            onTermsClick = {onEvent(SettingUiAction.OnTermsClicked)},
                            onPrivacyClick = {onEvent(SettingUiAction.OnPrivacyClicked)}
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = Color(0x1A000000), // 약한 테두리 (border-gray-200/50 느낌)
                shape = RoundedCornerShape(16.dp)
            ),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun ProfileCard(
    user: User,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Tailwind: bg-white/80 backdrop-blur-sm border-0 shadow-lg
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(width = 0.dp, color = Color.Transparent),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
        tonalElevation = 4.dp, // shadow-lg 유사
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person, // 대체: User 아이콘
                    contentDescription = "프로필 아이콘",
                    tint = Color(0xFF2563EB), // blue-600
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "프로필 정보",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            // Content
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDBEAFE)), // blue-100
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "배",
                        color = Color(0xFF1E40AF), // blue-800
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // User Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "배려이웃",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F2937) // gray-800
                        )
                    )
                    Text(
                        text = String.format("%s동 %s호", user.buildingNumber, user.unitNumber),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF4B5563) // gray-600
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Edit Button
                OutlinedButton(
                    onClick = onEditClick,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "수정",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("수정", fontSize = 14.sp)
                }
            }
        }
    }
}


// TODO: 각 event 채우기!
@Composable
fun NotificationSettingsSection(
    uiState: SettingUiState,
    onEvent: (SettingUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header (CardHeader + CardTitle 대응)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications, // Bell 대체
                contentDescription = "알림",
                tint = Color(0xFFCA8A04), // yellow-600 근사
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "알림 설정",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // 1) 푸시 알림
            SettingToggleRow(
                title = "푸시 알림",
                subtitle = "전체 알림 받기",
                checked = uiState.canNotifyAll,
                onCheckedChange = {
                    onEvent(SettingUiAction.OnToggleNotifyAll(it))
                }
            )

            // Separator (원본에서 첫 항목 뒤에만 구분선)
            Divider(color = Color(0xFFE5E7EB)) // gray-200 근사

            // 2) 소음 확인 요청 알림
            SettingToggleRow(
                title = "소음 확인",
                subtitle = "이웃의 소음 문의 알림",
                checked = uiState.canNotifyNoiseVote,
                onCheckedChange = {
                    onEvent(SettingUiAction.OnToggleNotifyNoiseVote(it))
                }
            )

            // 3) 공지 사항
            SettingToggleRow(
                title = "사전 양해 알림",
                subtitle = "주민들의 사전 양해",
                checked = uiState.canNotifyPreConsideration,
                onCheckedChange = {
                    onEvent(SettingUiAction.OnToggleNotifyPreConsideration(it))
                }
            )

            // 4) 사전 양해
            SettingToggleRow(
                title = "공지사항 알림",
                subtitle = "새 글 및 댓글 알림",
                checked = uiState.canNotifyCommunity,
                onCheckedChange = {
                    onEvent(SettingUiAction.OnToggleNotifyCommunity(it))
                }
            )
        }
    }
}

/** 한 줄짜리 토글 행 (제목/부제 + Switch) */
@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF1F2937), // gray-800
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF6B7280) // gray-500
                ),
                lineHeight = 16.sp
            )
        }
        Spacer(Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.scale(0.8f), // 🔹 크기 줄이기 (기본 대비 80%)
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,        // 토글 동그라미 (on)
                checkedTrackColor = Color(0xFF000000),  // 토글 배경 (on)
                uncheckedThumbColor = Color.White,// off 동그라미 (gray-400)
                uncheckedTrackColor = Color(0xFFE5E7EB),// off 배경 (gray-200)
                uncheckedBorderColor = Color.Transparent,
                checkedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun AccountManagementSection(
    onChangeAddress: () -> Unit,
    onChangePassword: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Shield,
                contentDescription = "계정 관리",
                tint = Color(0xFF16A34A), // green-600
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "계정 관리",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        // Content
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SettingRow(
                icon = Icons.Filled.Home,
                title = "거주지 변경",
                onClick = onChangeAddress
            )
            SettingRow(
                icon = Icons.Filled.Lock,
                title = "비밀번호 변경",
                onClick = onChangePassword
            )

            Divider(color = Color(0xFFE5E7EB)) // gray-200

            // 로그아웃 / 회원 탈퇴
            TextButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFDC2626)) // red-600
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "로그아웃",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("로그아웃")
                }
            }

            TextButton(
                onClick = onDeleteAccount,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFDC2626))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.DeleteForever,
                        contentDescription = "회원 탈퇴",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("회원 탈퇴")
                }
            }
        }
    }
}

// ----------------------------
// 앱 정보 섹션
// ----------------------------
@Composable
fun AppInfoSection(
    appVersion: String = "v1.0.0",
    onNoticeClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "앱 정보",
                tint = Color(0xFF9333EA), // purple-600
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "앱 정보",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        // Content
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // 버전 정보
            TextButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Unspecified)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("버전 정보", color = Color(0xFF1F2937))
                    Text(appVersion, color = Color(0xFF6B7280))
                }
            }

            SettingRow(title = "공지사항", onClick = onNoticeClick)
            SettingRow(title = "서비스 이용약관", onClick = onTermsClick)
            SettingRow(title = "개인정보 처리방침", onClick = onPrivacyClick)
        }
    }
}

// ----------------------------
// 재사용 가능한 단일 설정 행
// ----------------------------
@Composable
private fun SettingRow(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        colors = ButtonDefaults.textButtonColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = Color(0xFF4B5563), // gray-600
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Text(
                    text = title,
                    color = Color(0xFF1F2937), // gray-800
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF9CA3AF), // gray-400
                modifier = Modifier.size(20.dp)
            )
        }
    }
}