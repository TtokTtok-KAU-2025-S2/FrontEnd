package com.kau.ttokttok._core.network.di

import com.kau.ttokttok.data.remote.repository.*
import com.kau.ttokttok.domain.repository.*
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

    @Binds
    @Singleton
    abstract fun bindsSettingRepository(
        impl: SettingRepositoryImpl
    ): SettingRepository

    @Binds
    @Singleton
    abstract fun bindRecordingRepository(
        impl: RecordingRepositoryImpl
    ): RecordingRepository

    @Binds
    @Singleton
    abstract fun bindAIRepository(
        impl: AIRepositoryImpl
    ): AIRepository
}