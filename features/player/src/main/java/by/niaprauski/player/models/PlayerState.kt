package by.niaprauski.player.models


data class PlayerState(
    val isLoading: Boolean,
    val isShowWelcomeDialog: Boolean,
    val isShowPermissionInformationDialog: Boolean,
    val trackCount: Int,
    val isVisuallyEnabled: Boolean,
) {
    companion object {

        val DEFAULT = PlayerState(
            isLoading = true,
            isShowWelcomeDialog = false,
            isShowPermissionInformationDialog = false,
            trackCount = 0,
            isVisuallyEnabled = true,
        )
    }
}