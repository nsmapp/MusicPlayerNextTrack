package by.niaprauski.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.niaprauski.domain.usecases.settings.ChangeNightModeUseCase
import by.niaprauski.domain.usecases.settings.GetNightModeStateFlowUseCase
import by.niaprauski.settings.models.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val changeNightModeUseCase: ChangeNightModeUseCase,
    private val getNightModeStateFlowUseCase: GetNightModeStateFlowUseCase,
) : ViewModel(), SettingsContract {

    private val _state = MutableStateFlow(SettingsState.INITIAL)
    val state = _state.asStateFlow()

    init {
        getNightModeFlow()
    }


    override fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            changeNightModeUseCase.invoke(enabled)
                .onSuccess {
                    _state.update { it.copy(isNightMode = enabled) }
                }
        }
    }

    override fun getNightModeFlow() {
        viewModelScope.launch {
            val isNightMode = getNightModeStateFlowUseCase.invoke().first()
            _state.update { it.copy(isNightMode) }
        }
    }


}