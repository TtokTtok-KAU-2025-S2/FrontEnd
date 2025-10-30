package com.kau.ttokttok._core.network.di

import android.content.Context
import com.kau.ttokttok.data.remote.api.AuthApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    // ───────────────────────────────
    // 1️⃣ 기본 로깅 인터셉터
    // ───────────────────────────────
    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // ───────────────────────────────
    // 2️⃣ NoAuth 헤더 인터셉터 (로그인/회원가입용)
    // ───────────────────────────────
    @Provides
    @Singleton
    @Named("noAuthInterceptor")
    fun provideNoAuthHeaderInterceptor(
        @ApplicationContext context: Context
    ): Interceptor = Interceptor { chain ->
        val builder = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
        chain.proceed(builder.build())
    }

    // ───────────────────────────────
    // 3️⃣ Auth 헤더 인터셉터 (JWT 필요 API)
    // ───────────────────────────────
    @Provides
    @Singleton
    @Named("authInterceptor")
    fun provideAuthHeaderInterceptor(
        @ApplicationContext context: Context,
        // TODO: TokenProvider 주입받아서 Authorization 헤더 추가 예정
    ): Interceptor = Interceptor { chain ->
        val builder = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
        // builder.addHeader("Authorization", "Bearer ${tokenProvider.tokenOrNull()}")
        chain.proceed(builder.build())
    }

    // ───────────────────────────────
    // 4️⃣ OkHttpClient 분리
    // ───────────────────────────────
    @Provides
    @Singleton
    @Named("noAuthClient")
    fun provideNoAuthOkHttpClient(
        logging: HttpLoggingInterceptor,
        @Named("noAuthInterceptor") headerInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(logging)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    @Named("authClient")
    fun provideAuthOkHttpClient(
        logging: HttpLoggingInterceptor,
        @Named("authInterceptor") headerInterceptor: Interceptor
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
    @Named("noAuthRetrofit")
    fun provideNoAuthRetrofit(
        @Named("noAuthClient") client: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(
        @Named("authClient") client: OkHttpClient,
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
        @Named("noAuthRetrofit") retrofit: Retrofit
    ): AuthApiService = retrofit.create(AuthApiService::class.java)
}
