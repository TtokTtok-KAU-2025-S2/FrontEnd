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

private const val USER_PREFS = "user_prefs"
val Context.userDataStore by preferencesDataStore(name = USER_PREFS)

class UserStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val BUILDING_NUMBER = stringPreferencesKey("building_number")
        val UNIT_NUMBER = stringPreferencesKey("unit_number")
    }

    val buildingNumber: Flow<String?> = context.userDataStore.data.map { it[Keys.BUILDING_NUMBER] }
    val unitNumber: Flow<String?> = context.userDataStore.data.map { it[Keys.UNIT_NUMBER] }

    suspend fun getBuildingNumber(): String? = context.userDataStore.data.first()[Keys.BUILDING_NUMBER]

    suspend fun setBuildingNumber(buildingNumber: String) {
        context.userDataStore.edit { prefs ->
            prefs[Keys.BUILDING_NUMBER] = buildingNumber
        }
    }

    suspend fun deleteBuildingNumber() {
        context.userDataStore.edit { prefs ->
            prefs.remove(Keys.BUILDING_NUMBER)
        }
    }

    suspend fun getUnitNumber(): String? = context.userDataStore.data.first()[Keys.UNIT_NUMBER]

    suspend fun setUnitNumber(unitNumber: String) {
        context.userDataStore.edit { prefs ->
            prefs[Keys.UNIT_NUMBER] = unitNumber
        }
    }

    suspend fun deleteUnitNumber() {
        context.userDataStore.edit { prefs ->
            prefs.remove(Keys.UNIT_NUMBER)
        }
    }
}