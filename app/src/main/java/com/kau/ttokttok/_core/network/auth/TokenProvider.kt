package com.kau.ttokttok._core.network.auth

import com.kau.ttokttok.data.local.storage.AuthStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProvider @Inject constructor(
    private val authStorage: AuthStorage
) {
    @Volatile private var cachedJWT: String? = null
    @Volatile private var cachedRefresh: String? = null

    suspend fun init() {
        cachedJWT = authStorage.readJWT()
    }

    fun getTokenOrNull(): String? = cachedJWT

    suspend fun update(jwt: String, refresh: String? = null) {
        authStorage.save(jwt, refresh ?: cachedRefresh.orEmpty())

        cachedJWT = jwt

        if (refresh != null) {
            cachedRefresh = refresh
        }
    }

    suspend fun clear() {
        authStorage.clear()
        cachedJWT = null
        cachedRefresh = null
    }
}