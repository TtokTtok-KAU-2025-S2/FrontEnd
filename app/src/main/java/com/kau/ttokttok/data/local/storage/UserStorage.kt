package com.kau.ttokttok.data.local.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
        val BUILDING_NUMBER = intPreferencesKey("building_number")
        val UNIT_NUMBER = intPreferencesKey("unit_number")
    }

    val buildingNumber: Flow<Int?> = context.userDataStore.data.map { it[Keys.BUILDING_NUMBER] }
    val unitNumber: Flow<Int?> = context.userDataStore.data.map { it[Keys.UNIT_NUMBER] }

    suspend fun getBuildingNumber(): Int? = context.userDataStore.data.first()[Keys.BUILDING_NUMBER]

    suspend fun setBuildingNumber(buildingNumber: Int) {
        context.userDataStore.edit { prefs ->
            prefs[Keys.BUILDING_NUMBER] = buildingNumber
        }
    }

    suspend fun deleteBuildingNumber() {
        context.userDataStore.edit { prefs ->
            prefs.remove(Keys.BUILDING_NUMBER)
        }
    }

    suspend fun getUnitNumber(): Int? = context.userDataStore.data.first()[Keys.UNIT_NUMBER]

    suspend fun setUnitNumber(unitNumber: Int) {
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