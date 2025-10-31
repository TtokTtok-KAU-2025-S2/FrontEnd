package com.kau.ttokttok._core.network.auth

import com.kau.ttokttok.data.local.storage.UserStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProvider @Inject constructor(
    private val userStorage: UserStorage
){
    @Volatile private var cachedBuildingNumber: String? = null
    @Volatile private var cachedUnitNumber: String? = null

    suspend fun init() {
        cachedBuildingNumber = userStorage.getBuildingNumber()
        cachedUnitNumber = userStorage.getUnitNumber()
    }

    fun getBuildingNumberOrNull(): String? = cachedBuildingNumber

    suspend fun setBuildingNumber(buildingNumber: String) {
        userStorage.setBuildingNumber(buildingNumber)

        cachedBuildingNumber = buildingNumber
    }

    fun getUnitNumberOrNull(): String? = cachedUnitNumber

    suspend fun setUnitNumber(unitNumber: String) {
        userStorage.setUnitNumber(unitNumber)

        cachedUnitNumber = unitNumber
    }

    suspend fun clear() {
        userStorage.deleteBuildingNumber()
        userStorage.deleteUnitNumber()

        cachedBuildingNumber = null
        cachedUnitNumber = null
    }
}