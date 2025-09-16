package by.niaprauski.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


val Context.playerDatastore: DataStore<Preferences> by preferencesDataStore(name = "player_data_store")