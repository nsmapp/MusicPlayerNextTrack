package by.niaprauski.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import by.niaprauski.data.datastore.serializer.AppSettingsSerializer

private const val APP_SETTINGS_FILE_NAME = "app_settings.pb"

val Context.appSettingsStore: DataStore<AppSettingsEntity> by dataStore(
    fileName = APP_SETTINGS_FILE_NAME,
    serializer = AppSettingsSerializer
)