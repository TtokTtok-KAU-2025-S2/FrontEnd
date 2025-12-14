package com.kau.ttokttok.ui.compose.community

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

class CommunityFragment : Fragment() {
    private val viewModel: CommunityViewModel by viewModels()
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
            CommunityRoute(
                onClickCreatePost = { findNavController().navigateTo(Destination.WRITING_COMMUNITY)},
                onClickPost = { id ->
                    val args = Bundle().apply {
                        putLong("communityId", id)
                    }

                    findNavController().navigateTo(
                        dest = Destination.COMMUNITY_DETAIL,
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
            viewModel.loadPosts()
        }
    }
}