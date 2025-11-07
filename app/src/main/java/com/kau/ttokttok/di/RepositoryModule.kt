package com.kau.ttokttok.di

import com.kau.ttokttok.data.local.repository.NoiseLogRepositoryImpl
import com.kau.ttokttok.data.local.repository.NotificationRepositoryImpl
import com.kau.ttokttok.domain.repository.NoiseLogRepository
import com.kau.ttokttok.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository DI 모듈
 * 인터페이스와 구현체를 바인딩
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

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
}
