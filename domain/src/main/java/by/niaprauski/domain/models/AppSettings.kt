package by.niaprauski.domain.models

data class AppSettings(
    val isShowWelcomeMessage: Boolean,
    val isDarkMode: Boolean,
    val isVisuallyEnabled: Boolean,
    val minDuration: Int,
    val maxDuration: Int,
)