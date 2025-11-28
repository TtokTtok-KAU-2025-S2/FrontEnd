package com.kau.ttokttok._core.domain

import com.kau.ttokttok.data.local.repository.CommunityRepositoryImpl
import com.kau.ttokttok.data.local.repository.NoiseVoteRepositoryImpl
import com.kau.ttokttok.data.local.repository.PreConsiderationRepositoryImpl
import com.kau.ttokttok.data.local.repository.ReportRepositoryImpl
import com.kau.ttokttok.domain.usecase.community.CreatePostUseCase
import com.kau.ttokttok.domain.usecase.community.GetPostDetailUseCase
import com.kau.ttokttok.domain.usecase.community.LoadPostsUseCase
import com.kau.ttokttok.domain.usecase.monthreport.GetMonthReportUseCase
import com.kau.ttokttok.domain.usecase.noisevote.LoadPostsNoiseVoteUseCase
import com.kau.ttokttok.domain.usecase.preconsideration.DeletePostPreConsiderationUseCase
import com.kau.ttokttok.domain.usecase.preconsideration.LoadPostDetailPreConsiderationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    // 소음 현황판
    @Provides
    fun provideLoadPostsNoiseVoteUseCase(
        repository: NoiseVoteRepositoryImpl
    ): LoadPostsNoiseVoteUseCase {
        return LoadPostsNoiseVoteUseCase(repository)
    }

    // 사전 양해 게시판
    @Provides
    fun provideLoadPostDetailPreConsiderationUseCase(
        repository: PreConsiderationRepositoryImpl
    ): LoadPostDetailPreConsiderationUseCase {
        return LoadPostDetailPreConsiderationUseCase(repository)
    }

    @Provides
    fun provideDeletePostPreConsiderationUseCase(
        repository: PreConsiderationRepositoryImpl
    ): DeletePostPreConsiderationUseCase {
        return DeletePostPreConsiderationUseCase(repository)
    }

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