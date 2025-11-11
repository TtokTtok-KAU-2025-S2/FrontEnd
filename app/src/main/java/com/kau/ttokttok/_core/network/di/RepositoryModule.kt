package com.kau.ttokttok._core.network.di

import com.kau.ttokttok.data.local.repository.AuthRepositoryImpl
import com.kau.ttokttok.data.local.repository.CommunityRepositoryImpl
import com.kau.ttokttok.data.local.repository.NoiseVoteRepositoryImpl
import com.kau.ttokttok.data.local.repository.NoiseLogRepositoryImpl
import com.kau.ttokttok.data.local.repository.NotificationRepositoryImpl
import com.kau.ttokttok.data.local.repository.PreConsiderationRepositoryImpl
import com.kau.ttokttok.data.local.repository.ReportRepositoryImpl
import com.kau.ttokttok.domain.repository.AuthRepository
import com.kau.ttokttok.domain.repository.CommunityRepository
import com.kau.ttokttok.domain.repository.NoiseVoteRepository
import com.kau.ttokttok.domain.repository.NoiseLogRepository
import com.kau.ttokttok.domain.repository.NotificationRepository
import com.kau.ttokttok.domain.repository.PreConsiderationRepository
import com.kau.ttokttok.domain.repository.ReportRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCommunityRepository(
        impl: CommunityRepositoryImpl
    ): CommunityRepository

    @Binds
    @Singleton
    abstract fun bindNoiseBoardRepository(
        impl: NoiseVoteRepositoryImpl
    ): NoiseVoteRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindNoiseLogRepository(
        impl: NoiseLogRepositoryImpl
    ): NoiseLogRepository

    @Binds
    @Singleton
    abstract fun bindPreConsiderationRepository(
        impl: PreConsiderationRepositoryImpl
    ): PreConsiderationRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(
        impl: ReportRepositoryImpl
    ): ReportRepository
}