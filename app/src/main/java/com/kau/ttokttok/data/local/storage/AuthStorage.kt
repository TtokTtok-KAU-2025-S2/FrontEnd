package com.kau.ttokttok.data.local.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val AUTH_PREFS = "auth_prefs"
val Context.authDataStore by preferencesDataStore(name = AUTH_PREFS)

// TODO: DataStore 보안 추가하기
class AuthStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val JWT = stringPreferencesKey("jwt_token")
        val REFRESH = stringPreferencesKey("refresh_token")
    }

    val jwt: Flow<String?> = context.authDataStore.data.map { it[Keys.JWT] }
    val refresh: Flow<String?> = context.authDataStore.data.map { it[Keys.REFRESH] }

    suspend fun save(jwt: String, refresh: String) {
        context.authDataStore.edit { prefs ->
            prefs[Keys.JWT] = jwt
            prefs[Keys.REFRESH] = refresh
        }
    }

    suspend fun readJWT(): String? = context.authDataStore.data.first()[Keys.JWT]

    // (선택) refresh 읽기함수 있으면 편함
    suspend fun readRefresh(): String? = context.authDataStore.data.first()[Keys.REFRESH]

    suspend fun clear() {
        context.authDataStore.edit { prefs ->
            prefs.remove(Keys.JWT)
            prefs.remove(Keys.REFRESH)
        }
    }
}
