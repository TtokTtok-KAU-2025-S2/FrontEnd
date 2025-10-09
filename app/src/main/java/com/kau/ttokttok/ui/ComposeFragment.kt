package com.kau.ttokttok.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.kau.ttokttok.ui.compose.login.LoginRoute
import dagger.hilt.android.AndroidEntryPoint

// Copmpose 전용 Fragment
// 기존 XML Fragment처럼 쓸 수 있지만 내부를 Compose로 그린다.
@AndroidEntryPoint
class ComposeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Fragment가 destroy될 때 Compose 리소스를 자동 정리
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            // Compose 코드 영억 시작
            setContent {
                MaterialTheme {
                    LoginRoute()
                }
            }
        }
    }
    companion object {
        // Fragment 생성 도우미
        fun newInstance() = ComposeFragment()
    }
}