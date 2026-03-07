package by.niaprauski.navigation.screens.settings

import by.niaprauski.navigation.Dest
import kotlinx.serialization.Serializable

@Serializable
sealed class SettingsDest: Dest {

    companion object{
        fun root() = Settings
    }

    @Serializable
    object Settings: SettingsDest()
}