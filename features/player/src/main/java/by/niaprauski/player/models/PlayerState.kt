package by.niaprauski.player.models


data class PlayerState(
    val isShowWelcomeDialog: Boolean,
    val isShowPermissionInformationDialog: Boolean,
    val trackCount: Int,
    val isVisuallyEnabled: Boolean,
    val isSyncing: Boolean = false,
) {
    companion object {

        val DEFAULT = PlayerState(
            isShowWelcomeDialog = false,
            isShowPermissionInformationDialog = false,
            trackCount = 0,
            isVisuallyEnabled = true,
        )
    }
}