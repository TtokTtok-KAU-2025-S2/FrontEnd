package com.kau.ttokttok._core.network.auth

import com.kau.ttokttok.data.local.storage.UserStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProvider @Inject constructor(
    private val userStorage: UserStorage
){
    @Volatile private var cachedBuildingNumber: Int? = null
    @Volatile private var cachedUnitNumber: Int? = null

    suspend fun init() {
        cachedBuildingNumber = userStorage.getBuildingNumber()
        cachedUnitNumber = userStorage.getUnitNumber()
    }

    fun getBuildingNumberOrNull(): Int? = cachedBuildingNumber

    suspend fun setBuildingNumber(buildingNumber: Int) {
        userStorage.setBuildingNumber(buildingNumber)

        cachedBuildingNumber = buildingNumber
    }

    fun getUnitNumberOrNull(): Int? = cachedUnitNumber

    suspend fun setUnitNumber(unitNumber: Int) {
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