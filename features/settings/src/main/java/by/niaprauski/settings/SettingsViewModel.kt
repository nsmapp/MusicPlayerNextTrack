package by.niaprauski.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.niaprauski.domain.models.AppSettings
import by.niaprauski.domain.usecases.settings.ChangeNightModeUseCase
import by.niaprauski.domain.usecases.settings.GetSettingsUseCase
import by.niaprauski.domain.usecases.settings.SetMaxTrackDurationUseCase
import by.niaprauski.domain.usecases.settings.SetMinTrackDurationUseCase
import by.niaprauski.domain.usecases.settings.SetVisualizerStatusUseCase
import by.niaprauski.settings.models.SettingsState
import by.niaprauski.utils.extension.convertToInt
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val changeNightModeUseCase: ChangeNightModeUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val setVisualizerStatusUseCase: SetVisualizerStatusUseCase,
    private val setMinDurationUseCase: SetMinTrackDurationUseCase,
    private val setMaxDurationUseCase: SetMaxTrackDurationUseCase,
) : ViewModel(), SettingsContract {

    companion object {
        private const val INPUT_DEBOUNCE = 500L
        private const val SEK_MLS = 1000
        private const val MIN_MLS = 60000
    }

    private val _state = MutableStateFlow(SettingsState.INITIAL)
    val state = _state.asStateFlow()

    private val _minDuration = MutableSharedFlow<Int>()
    private val _maxDuration = MutableSharedFlow<Int>()


    fun onLaunch() {
        getSettings()
    }

    override fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            changeNightModeUseCase.invoke(enabled)
                .onSuccess {
                    _state.update { it.copy(isDarkMode = enabled) }
                }
        }
    }

    override fun getSettings() {
        viewModelScope.launch {
            getSettingsUseCase.invoke()
                .onSuccess { settings ->
                    handleSettingsWithCollectChanges(settings)
                }
                .onFailure {
                }
        }
    }

    private fun handleSettingsWithCollectChanges(settings: AppSettings) {
        with(settings) {
            _state.update {
                it.copy(
                    isDarkMode = isDarkMode,
                    isVisuallyEnabled = isVisuallyEnabled,
                    minDuration = (minDuration / SEK_MLS).toString(),
                    isMinDurationError = false,
                    maxDuration = (maxDuration / MIN_MLS).toString(),
                    isMaxDurationError = false,
                )
            }
        }

        startCollectMinDuration()
        startCollectMaxDuration()
    }


    private fun startCollectMinDuration() {
        viewModelScope.launch {
            _minDuration
                .debounce(INPUT_DEBOUNCE)
                .collect { duration ->
                    setMinDurationUseCase.invoke(duration)
                        .onFailure {
                            _state.update { it.copy(isMinDurationError = true) }
                        }
                }
        }
    }

    private fun startCollectMaxDuration() {
        viewModelScope.launch {
            _maxDuration
                .debounce(INPUT_DEBOUNCE)
                .collect { duration ->
                    setMaxDurationUseCase.invoke(duration)
                        .onFailure {
                            _state.update { it.copy(isMaxDurationError = true) }
                        }
                }
        }
    }

    override fun setVisuallyEnabled(enabled: Boolean) {
        viewModelScope.launch {
            setVisualizerStatusUseCase.set(enabled)
                .onSuccess { _state.update { it.copy(isVisuallyEnabled = enabled) } }
        }

    }

    override fun setMinDuration(duration: String) {
        if (duration.length > 2 ) return
        _state.update { it.copy(minDuration = duration, isMinDurationError = false) }
        if (duration.isEmpty() ) return

        val durationLong = duration.convertToInt()
        viewModelScope.launch {
            _minDuration.emit(durationLong)
        }
    }

    override fun setMaxDuration(duration: String) {
        if (duration.length > 2 ) return
        _state.update { it.copy(maxDuration = duration, isMaxDurationError = false) }
        if (duration.isEmpty()) return

        val durationLong = duration.convertToInt()
        viewModelScope.launch {
            _maxDuration.emit(durationLong)
        }
    }

}