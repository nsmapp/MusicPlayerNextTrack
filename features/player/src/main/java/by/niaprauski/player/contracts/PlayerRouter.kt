package by.niaprauski.player.contracts

import android.content.Context

interface PlayerRouter {

    fun openSettings()

    fun openLibrary()

    fun openAppSettings(context: Context)
}