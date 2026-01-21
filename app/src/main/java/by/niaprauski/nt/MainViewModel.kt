package by.niaprauski.nt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.niaprauski.domain.usecases.settings.GetSettingsFlowUseCase
import by.niaprauski.nt.models.MainState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getSettingsFlowUseCase: GetSettingsFlowUseCase,
): ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.INITIAL)
    val state = _state.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            getSettingsFlowUseCase.invoke()
                .collect { settings ->
                _state.update {  it.copy(isNightMode = settings.isDarkMode) }
                }
        }
    }

}