package com.kau.ttokttok.ui.compose.noisevote.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.kau.ttokttok.ui.navigation.Destination
import com.kau.ttokttok.ui.navigation.navigateTo

class NoiseVoteDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        setContent {
            NoiseVoteDetailRoute(
                onClickBack = { findNavController().popBackStack() },
                onClickCommentModify = { id, content ->
                    val args = Bundle().apply {
                        putLong("noiseVoteCommentId", id)
                        putString("noiseVoteCommentContent", content)
                    }

                    findNavController().navigateTo(
                        dest = Destination.NOISE_VOTE_COMMENT_MODIFY,
                        args = args
                    )
                }
            )
        }
    }
}