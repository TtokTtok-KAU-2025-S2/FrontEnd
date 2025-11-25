package com.kau.ttokttok.ui.compose.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kau.ttokttok._core.network.result.NetworkResult
import com.kau.ttokttok.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface RegisterEvent {
    data object NavigateHome : RegisterEvent
    data class ShowMessage(val message: String) : RegisterEvent
    data class ShowAlert(val title: String, val message: String) : RegisterEvent
}
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private val _events = MutableSharedFlow<RegisterEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<RegisterEvent> = _events.asSharedFlow()

    // TODO: 리팩토링 (ViewModel에서 입력검사 X -> UseCase로 옮기기)
    fun onClickRegister(
        aptId: String,
        email: String,
        password: String,
        buildingNumber: String,
        unitNumber: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                // 동/호수 입력 검증 및 파싱
                val parsedAptId = parseApartmentId(aptId)
                val parsedBuildingNumber = parseBuildingNumber(buildingNumber)
                val parsedUnitNumber = parseUnitNumber(unitNumber)

                // BE 연결
                when (val r = authUseCase.register(parsedAptId, email, password, parsedBuildingNumber, parsedUnitNumber)) {
                    is NetworkResult.Success -> {

                        emit(RegisterEvent.NavigateHome)
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }

                    is NetworkResult.Error -> {
                        val message = r.message ?: r.exception?.message ?: "회원가입에 실패했습니다."

                        _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
                        emit(RegisterEvent.ShowAlert("회원가입 실패", message))
                    }
                }
            } catch (e: IllegalArgumentException) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
                emit(RegisterEvent.ShowAlert("회원가입 실패", e.message ?: "잘못된 입력입니다."))
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
                emit(RegisterEvent.ShowAlert("회원가입 실패", e.message ?: "알 수 없는 오류입니다."))
            }
        }
    }

    private fun emit(event: RegisterEvent) {
        _events.tryEmit(event)
    }

    private fun parseApartmentId(aptId: String): Long {
        return aptId.toLongOrNull() ?: throw java.lang.IllegalArgumentException("숫자 형식이 아닙니다.")
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