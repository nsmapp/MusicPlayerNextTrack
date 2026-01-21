package by.niaprauski.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.niaprauski.domain.usecases.settings.ChangeNightModeUseCase
import by.niaprauski.domain.usecases.settings.GetSettingsFlowUseCase
import by.niaprauski.domain.usecases.settings.SetVisualizerStatusUseCase
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
    private val getSettingsFlowUseCase: GetSettingsFlowUseCase,
    private val setVisualizerStatusUseCase: SetVisualizerStatusUseCase
) : ViewModel(), SettingsContract {

    private val _state = MutableStateFlow(SettingsState.INITIAL)
    val state = _state.asStateFlow()

    init {
        getSettingsFlow()
    }


    override fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            changeNightModeUseCase.invoke(enabled)
                .onSuccess {
                    _state.update { it.copy(isDarkMode = enabled) }
                }
        }
    }

    override fun getSettingsFlow() {
        viewModelScope.launch {
            val settings = getSettingsFlowUseCase.invoke().first()
            _state.update {
                it.copy(
                    isDarkMode = settings.isDarkMode,
                    isVisuallyEnabled = settings.isVisuallyEnabled
                )
            }
        }
    }

    override fun setVisuallyEnabled(enabled: Boolean) {
        viewModelScope.launch {
            setVisualizerStatusUseCase.set(enabled)
                .onSuccess { _state.update { it.copy(isVisuallyEnabled = enabled) } }
        }

    }


}