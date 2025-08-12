package by.niaprauski.playerservice.models

import androidx.media3.common.Player

enum class RepeatMode(val value: Int) {

    REPEAT_MODE_OFF(Player.REPEAT_MODE_OFF),
    REPEAT_MODE_ONE(Player.REPEAT_MODE_ONE),
    REPEAT_MODE_ALL(Player.REPEAT_MODE_ALL),
}