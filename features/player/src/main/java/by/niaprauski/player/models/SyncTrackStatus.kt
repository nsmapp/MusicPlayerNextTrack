package by.niaprauski.player.models

import by.niaprauski.utils.models.ITrack

sealed class SyncTrackStatus{

    object Started : SyncTrackStatus()
    data class Finished(val tracks: List<ITrack>): SyncTrackStatus()
}