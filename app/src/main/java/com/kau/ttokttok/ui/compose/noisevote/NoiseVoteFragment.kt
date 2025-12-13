package com.kau.ttokttok.ui.compose.noisevote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kau.ttokttok.ui.navigation.Destination
import com.kau.ttokttok.ui.navigation.navigateTo

class NoiseVoteFragment : Fragment() {
    private val viewModel: NoiseVoteViewModel by viewModels()
    private var first = true

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
                onClickPost = { id ->
                    val args = Bundle().apply {
                        putLong("noiseVoteId", id)
                    }

                    findNavController().navigateTo(Destination.NOISE_VOTE_DETAIL, args = args)
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()

        if (first) {
            first = false
        }

        else {
            viewModel.loadPosts()
        }
    }
}