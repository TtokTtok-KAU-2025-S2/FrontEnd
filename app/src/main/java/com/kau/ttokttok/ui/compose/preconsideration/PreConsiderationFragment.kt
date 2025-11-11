package com.kau.ttokttok.ui.compose.preconsideration

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

class PreConsiderationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        setContent {
            PreConsiderationRoute(
                onClickCreatePost = { findNavController().navigateTo(Destination.WRITING_PRECONSIDERATION)},
                onClickPost = { id ->
                    val args = Bundle().apply {
                        putLong("preConsiderationId", id)
                    }

                    findNavController().navigateTo(
                    dest = Destination.PRECONSIDERATION_DETAIL,
                    args = args
                ) }
            )
        }
    }
}