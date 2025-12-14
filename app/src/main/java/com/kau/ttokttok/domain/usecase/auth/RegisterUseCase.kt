package com.kau.ttokttok.domain.usecase.auth

import com.kau.ttokttok.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        aptId: String,
        email: String,
        password: String,
        buildingNumber: String,
        unitNumber: String
    ): Result<Unit> = repository.register(
            aptId = parseApartmentId(aptId),
            email = email,
            password = password,
            buildingNumber = parseBuildingNumber(buildingNumber),
            unitNumber = parseUnitNumber(unitNumber)
        )

    private fun parseApartmentId(aptId: String): Long {
        return aptId.toLongOrNull() ?: throw IllegalArgumentException("숫자 형식이 아닙니다.")
    }

    private fun parseBuildingNumber(buildingNumber: String) : Int {
        val trimmed = buildingNumber.trim()

        // "000동" 형태만 허용 (숫자 + '동')
        val match = Regex("^(\\d{1,4})동$").find(trimmed)
            ?: throw IllegalArgumentException("동 번호는 숫자 뒤에 '동'이 붙은 형식이어야 합니다. 예: 101동")

        val parsedBuildingNumber = match.groupValues[1].toIntOrNull()
            ?: throw IllegalArgumentException("올바른 동 번호를 입력해주세요.")

        if (parsedBuildingNumber <= 0) {
            throw IllegalArgumentException("동 번호는 1 이상이어야 합니다.")
        }

        return parsedBuildingNumber
    }

    private fun parseUnitNumber(unitNumber: String) : Int {
        val trimmed = unitNumber.trim()

        // "000호" 형태만 허용 (숫자 + '호')
        val match = Regex("^(\\d{1,4})호$").find(trimmed)
            ?: throw IllegalArgumentException("호 번호는 숫자 뒤에 '호'가 붙은 형식이어야 합니다. 예: 202호")

        val parsedUnitNumber = match.groupValues[1].toIntOrNull()
            ?: throw IllegalArgumentException("올바른 호 번호를 입력해주세요.")

        if (parsedUnitNumber <= 0) {
            throw IllegalArgumentException("호 번호는 1 이상이어야 합니다.")
        }

        return parsedUnitNumber
    }
}