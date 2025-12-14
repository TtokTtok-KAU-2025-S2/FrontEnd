package com.kau.ttokttok.domain.usecase.preconsideration

import com.kau.ttokttok.data.remote.repository.PreConsiderationRepositoryImpl
import com.kau.ttokttok.ui.compose.preconsideration.PreConsiderationPost
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPostsPreConsiderationUseCase @Inject constructor(
    private val repository: PreConsiderationRepositoryImpl
) {
    suspend operator fun invoke(): Result<List<PreConsiderationPost>> = repository.getPosts()
}