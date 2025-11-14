package com.kau.ttokttok.ui.compose.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingRoute(
    // 해당 SettingViewModel에 맞는 viewModel을 불러온다. (ex. Spring 컨테이너에 등록된 Bean을 자동으로 주입 (Autowired))
    viewModel: SettingViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    // viewModel의 uiState를 관찰 중
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 해당 화면을 실행할 때 실행한다. (ex. 클래스를 생성하면 생성자 로직이 진행되는 것과 유사하다)
    LaunchedEffect(viewModel) {
        viewModel.getSettingUi()

        // viewModel의 event를 관찰하고, 그에 맞게 event에 맞게 상호작용을 진행한다.
        viewModel.events.collect { event ->
            // TODO: 각 이벤트에 맞게 추가해야 하는 내용들 정리
            when (event) {
                else -> {

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
}