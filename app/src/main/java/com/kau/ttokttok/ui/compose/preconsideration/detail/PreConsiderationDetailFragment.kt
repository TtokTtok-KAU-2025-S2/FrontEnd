package com.kau.ttokttok.ui.compose.preconsideration.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.kau.ttokttok.ui.navigation.Destination
import com.kau.ttokttok.ui.navigation.navigateTo

class PreConsiderationDetailFragment : Fragment() {
    private val viewModel: PreConsiderationDetailViewModel by viewModels()
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
            PreConsiderationDetailRoute(
                onClickBack = { findNavController().popBackStack() },
                onEdit = { id ->
                    val args = Bundle().apply {
                        putLong("preConsiderationId", id)
                    }

                    findNavController().navigateTo(
                        dest = Destination.WRITING_PRECONSIDERATION,
                        args = args
                    )
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
            viewModel.loadPostDetail()
        }
    }
}