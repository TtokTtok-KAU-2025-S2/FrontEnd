package com.kau.ttokttok._core.domain

import com.kau.ttokttok.data.local.repository.CommunityRepositoryImpl
import com.kau.ttokttok.data.local.repository.ReportRepositoryImpl
import com.kau.ttokttok.domain.usecase.community.CreatePostUseCase
import com.kau.ttokttok.domain.usecase.community.GetPostDetailUseCase
import com.kau.ttokttok.domain.usecase.community.LoadPostsUseCase
import com.kau.ttokttok.domain.usecase.monthreport.GetMonthReportUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    // 공지 사항
    @Provides
    fun provideCreatePostUseCase(
        repository: CommunityRepositoryImpl
    ): CreatePostUseCase {
        return CreatePostUseCase(repository)
    }

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

    // 월간 리포트
    @Provides
    fun provideGetMonthReportUseCase(
        repository: ReportRepositoryImpl
    ): GetMonthReportUseCase {
        return GetMonthReportUseCase(repository)
    }
}