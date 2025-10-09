package com.kau.ttokttok.data.local.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val AUTH_PREFS = "auth_prefs"

// Context 확장 프로퍼티 (싱글톤처럼 사용)
val Context.authDataStore by preferencesDataStore(name = AUTH_PREFS)

// TODO: DataStore 보안 추가하기
class AuthStorage(private val context: Context) {
    private object Keys {
        val JWT = stringPreferencesKey("jwt_token")
        val REFRESH = stringPreferencesKey("refresh_token")
    }

    val jwt: Flow<String?> = context.authDataStore.data.map { prefs ->
        prefs[Keys.JWT]
    }

    val refresh: Flow<String?> = context.authDataStore.data.map { prefs ->
        prefs[Keys.REFRESH]
    }

    // 토큰 저장
    suspend fun save(jwt: String, refresh: String) {
        context.authDataStore.edit { prefs ->
            prefs[Keys.JWT] = jwt
            prefs[Keys.REFRESH] = refresh
        }
    }

    // 토큰 읽기
    suspend fun readJWT(): String? =
        context.authDataStore.data.first()[Keys.JWT]

    // 토큰 삭제
    suspend fun clear() {
        context.authDataStore.edit { prefs ->
            prefs.remove(Keys.JWT)
            prefs.remove(Keys.REFRESH)
        }
    }
}