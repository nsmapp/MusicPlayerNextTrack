package by.niaprauski.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import by.niaprauski.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepoImpl @Inject constructor(val store: DataStore<Preferences>): SettingsRepository {


    companion object {
        private val KEY_IS_DARK_MODE = booleanPreferencesKey("key_is_dark_mode")
        private val KEY_IS_NEED_WELCOME_MESSAGE = booleanPreferencesKey("key_is_first_launch")

    }


    override suspend fun getDarkModeFlow(): Flow<Boolean> = store.data
        .map { preferences -> preferences[KEY_IS_DARK_MODE] ?: false }

    override suspend fun setNightMode(isDarkMode: Boolean) {
        store.edit { preferences -> preferences[KEY_IS_DARK_MODE] = isDarkMode }
    }

    override suspend fun isShowWelcomeMessage(): Boolean = store.data
        .map { preferences -> preferences[KEY_IS_NEED_WELCOME_MESSAGE] ?: true }.first()


    override suspend fun setShowWelcomeMessage(isFirstLaunch: Boolean) {
        store.edit { preferences -> preferences[KEY_IS_NEED_WELCOME_MESSAGE] = isFirstLaunch }
    }}