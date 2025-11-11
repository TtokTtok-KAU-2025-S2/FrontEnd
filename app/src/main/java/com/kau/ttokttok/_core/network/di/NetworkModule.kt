package com.kau.ttokttok._core.network.di

import com.kau.ttokttok._core.network.auth.TokenProvider
import com.kau.ttokttok.data.remote.api.AuthApiService
import com.kau.ttokttok.data.remote.api.CommunityApiService
import com.kau.ttokttok.data.remote.api.NoiseCalendarApiService
import com.kau.ttokttok.data.remote.api.NoiseRecordApiService
import com.kau.ttokttok.data.remote.api.NoiseStatusBoardApiService
import com.kau.ttokttok.data.remote.api.PreNoticeApiService
import com.kau.ttokttok.data.remote.api.ReportApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://Ttokttok-dev-env.eba-dp3skxgf.ap-northeast-2.elasticbeanstalk.com/"

    // ───────────────────────────────
    // 1️⃣ 기본 로깅 인터셉터
    // ───────────────────────────────
    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // ───────────────────────────────
    // 헤더 인터셉터
    // ───────────────────────────────
    @Provides
    @Singleton
    fun provideAuthHeaderInterceptor(
        tokenProvider: TokenProvider
    ): Interceptor = Interceptor { chain ->
        val req = chain.request()

        val noAuth = req.header("No-Auth")?.equals("true", ignoreCase = true) == true

        val requestBuilder = req.newBuilder()
            .removeHeader("No-Auth")
            .addHeader("Accept", "application/json")

        if (req.body != null) {
            requestBuilder.addHeader("Content-Type", "application/json")
        }

        if (!noAuth) {
            requestBuilder.addHeader("Authorization", String.format("Bearer %s", tokenProvider.getTokenOrNull()))
        }

        chain.proceed(requestBuilder.build())
    }

    // ───────────────────────────────
    // OkHttpClient 분리
    // ───────────────────────────────
    @Provides
    @Singleton
    fun provideAuthOkHttpClient(
        logging: HttpLoggingInterceptor,
        headerInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(logging)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    // ───────────────────────────────
    // 5️⃣ Moshi 설정
    // ───────────────────────────────
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // ───────────────────────────────
    // 6️⃣ Retrofit 분리
    // ───────────────────────────────
    @Provides
    @Singleton
    fun provideAuthRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    // ───────────────────────────────
    // 7️⃣ API 서비스 제공 (noAuth용)
    // ───────────────────────────────
    @Provides
    @Singleton
    fun provideAuthApiService(
        retrofit: Retrofit
    ): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideCommunityApiService(
        retrofit: Retrofit
    ): CommunityApiService = retrofit.create(CommunityApiService::class.java)

    @Provides
    @Singleton
    fun provideNoiseCalendarApiService(
        retrofit: Retrofit
    ): NoiseCalendarApiService = retrofit.create(NoiseCalendarApiService::class.java)

    @Provides
    @Singleton
    fun provideNoiseRecordApiService(
        retrofit: Retrofit
    ): NoiseRecordApiService = retrofit.create(NoiseRecordApiService::class.java)

    @Provides
    @Singleton
    fun provideNoiseStatusBoardApiService(
        retrofit: Retrofit
    ): NoiseStatusBoardApiService = retrofit.create(NoiseStatusBoardApiService::class.java)

    @Provides
    @Singleton
    fun providePreNoticeApiService(
        retrofit: Retrofit
    ): PreNoticeApiService = retrofit.create(PreNoticeApiService::class.java)

    @Provides
    @Singleton
    fun provideReportApiService(
        retrofit: Retrofit
    ): ReportApiService = retrofit.create(ReportApiService::class.java)
}
