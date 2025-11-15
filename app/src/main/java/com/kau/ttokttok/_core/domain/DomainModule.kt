package com.kau.ttokttok._core.domain

import com.kau.ttokttok.data.local.repository.CommunityRepositoryImpl
import com.kau.ttokttok.domain.repository.CommunityRepository
import com.kau.ttokttok.domain.usecase.community.GetPostDetailUseCase
import com.kau.ttokttok.domain.usecase.community.LoadPostsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {


    // 공지 사항
    @Provides
    fun providesGetPostDetailUseCase(
        repository: CommunityRepositoryImpl
    ): GetPostDetailUseCase {
        return GetPostDetailUseCase(repository)
    }

    @Provides
    fun provideLoadPostsUseCase(
        repository: CommunityRepositoryImpl
    ): LoadPostsUseCase {
        return LoadPostsUseCase(repository)
    }
}