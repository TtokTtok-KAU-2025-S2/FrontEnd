package com.kau.ttokttok.ui.compose.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // 프래그먼트 생명주기와 함께 안전하게 정리
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                // MaterialTheme는 프로젝트 공용 theme을 적용할 수도 있음
                MaterialTheme {
                    LoginScreen(
                        onClickLogin = { _, _ -> /* TODO: ViewModel 연결 예정 */ },
                        onClickSignup = { /* TODO */ },
                        onClickFindPassword = { /* TODO */ }
                    )
                }
            }
        }
    }
}
