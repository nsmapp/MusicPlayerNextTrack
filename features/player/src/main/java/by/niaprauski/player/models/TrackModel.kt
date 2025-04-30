package by.niaprauski.player.models

import by.niaprauski.utils.models.ITrack

data class TrackModel(
    override val title: String,
    override val artist: String,
    override val path: String
): ITrack
