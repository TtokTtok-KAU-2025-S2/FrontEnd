package com.kau.ttokttok.ui.compose.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kau.ttokttok.ui.navigation.navigateTo
import com.kau.ttokttok.ui.navigation.resetToLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        setContent {
            SettingRoute(
                onBack = { findNavController().popBackStack() },
                onNavigate = { dest -> findNavController().navigateTo(dest) },
                onResetToLogin = { findNavController().resetToLogin() }
            )
        }
    }
}