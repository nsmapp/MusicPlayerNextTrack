package by.niaprauski.navigation.screens.settings

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import by.niaprauski.navigation.Navigator
import by.niaprauski.settings.SettingsScreen

fun EntryProviderScope<NavKey>.settings(navigator: Navigator) {

    entry<SettingsDest.Settings> {
        SettingsScreen()
    }
}