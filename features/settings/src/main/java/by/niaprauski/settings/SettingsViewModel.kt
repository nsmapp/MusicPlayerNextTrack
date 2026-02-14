package by.niaprauski.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.niaprauski.domain.models.AppSettings
import by.niaprauski.domain.usecases.settings.GetSettingsUseCase
import by.niaprauski.domain.usecases.settings.SetAccentColorUseCase
import by.niaprauski.domain.usecases.settings.SetBackgroundColorUseCase
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
    private val getSettingsUseCase: GetSettingsUseCase,
    private val setVisualizerStatusUseCase: SetVisualizerStatusUseCase,
    private val setMinDurationUseCase: SetMinTrackDurationUseCase,
    private val setMaxDurationUseCase: SetMaxTrackDurationUseCase,
    private val setAccentColorUseCase: SetAccentColorUseCase,
    private val setBackgroundColorUseCase: SetBackgroundColorUseCase,
) : ViewModel(), SettingsContract {

    companion object {
        private const val INPUT_DEBOUNCE_TEXT = 500L
        private const val INPUT_DEBOUNCE_COLOR = 100L
        private const val SEK_MLS = 1000
        private const val MIN_MLS = 60000
    }

    private val _state = MutableStateFlow(SettingsState.INITIAL)
    val state = _state.asStateFlow()

    private val _minDuration = MutableSharedFlow<Int>()
    private val _maxDuration = MutableSharedFlow<Int>()

    private val _accentPosition = MutableSharedFlow<Pair<String, Float>>()
    private val _backgroundPosition = MutableSharedFlow<Pair<String, Float>>()


    fun onLaunch() {
        getSettings()
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
                    isVisuallyEnabled = isVisuallyEnabled,
                    minDuration = (minDuration / SEK_MLS).toString(),
                    isMinDurationError = false,
                    maxDuration = (maxDuration / MIN_MLS).toString(),
                    isMaxDurationError = false,
                    acentPositon = accentPosition,
                    backgroundPosition = backgroundPosition,
                )
            }
        }

        startCollectMinDuration()
        startCollectMaxDuration()
        startCollectColorChanging()
    }


    private fun startCollectMinDuration() {
        viewModelScope.launch {
            _minDuration
                .debounce(INPUT_DEBOUNCE_TEXT)
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
                .debounce(INPUT_DEBOUNCE_TEXT)
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
        if (duration.length > 2) return
        _state.update { it.copy(minDuration = duration, isMinDurationError = false) }
        if (duration.isEmpty()) return

        val durationLong = duration.convertToInt()
        viewModelScope.launch {
            _minDuration.emit(durationLong)
        }
    }

    override fun setMaxDuration(duration: String) {
        if (duration.length > 2) return
        _state.update { it.copy(maxDuration = duration, isMaxDurationError = false) }
        if (duration.isEmpty()) return

        val durationLong = duration.convertToInt()
        viewModelScope.launch {
            _maxDuration.emit(durationLong)
        }
    }

    override fun setAccentColorSettings(hexColor: String, position: Float) {
        _state.update { it.copy(acentPositon = position) }
        viewModelScope.launch {
            _accentPosition.emit(hexColor to position)
        }

    }

    override fun setBackgroundColorSettings(hexColor: String, position: Float) {
        _state.update { it.copy(backgroundPosition = position) }
        viewModelScope.launch {
            _backgroundPosition.emit(hexColor to position)
        }
    }

    private fun saveBackgroundColorSettings(hexColor: String, position: Float) {
        viewModelScope.launch {
            setBackgroundColorUseCase.invoke(hexColor, position)
        }
    }

    private fun saveAccentColorSettings(hexColor: String, position: Float) {
        viewModelScope.launch {
            setAccentColorUseCase.invoke(hexColor, position)
        }
    }

    private fun startCollectColorChanging() {
        viewModelScope.launch {
            _accentPosition
                .debounce(INPUT_DEBOUNCE_COLOR)
                .collect { (hexColor, position) ->
                    saveAccentColorSettings(hexColor, position)
                }
        }

        viewModelScope.launch {
            _backgroundPosition
                .debounce(INPUT_DEBOUNCE_COLOR)
                .collect { (hexColor, position) ->
                    saveBackgroundColorSettings(hexColor, position)
                }
        }
    }

}