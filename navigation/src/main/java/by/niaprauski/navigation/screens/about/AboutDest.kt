package by.niaprauski.navigation.screens.about

import by.niaprauski.navigation.Dest
import kotlinx.serialization.Serializable

@Serializable
sealed class AboutDest: Dest {

    companion object{
        fun root() = About
    }

    @Serializable
    object About: AboutDest()
}