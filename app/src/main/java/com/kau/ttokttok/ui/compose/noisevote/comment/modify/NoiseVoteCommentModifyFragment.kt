package com.kau.ttokttok.ui.compose.noisevote.comment.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class NoiseVoteCommentModifyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        setContent {
            NoiseVoteCommentModifyRoute(
                onClickBack = {
                    findNavController().previousBackStackEntry?.
                    savedStateHandle?.set("needRefresh", true)

                    findNavController().popBackStack()
                }
            )
        }
    }
}