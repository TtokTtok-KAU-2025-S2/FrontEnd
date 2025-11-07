package com.kau.ttokttok.ui.compose.noisevote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.R
import androidx.navigation.fragment.findNavController
import com.kau.ttokttok.ui.navigation.Destination
import com.kau.ttokttok.ui.navigation.navigateTo

class NoiseVoteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        setContent {
            NoiseVoteRoute(
                onClickPost = { findNavController().navigateTo(Destination.NOISE_VOTE_DETAIL)}
            )
        }
    }
}