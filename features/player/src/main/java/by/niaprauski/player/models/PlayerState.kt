package by.niaprauski.player.models


data class PlayerState(
    val isLoading: Boolean,
    val isShowWelcomeDialog: Boolean,
    val isShowPermissionInformationDialog: Boolean,
    val trackCount: Int,
) {
    companion object {

        val INITIAL = PlayerState(
            isLoading = true,
            isShowWelcomeDialog = false,
            isShowPermissionInformationDialog = false,
            trackCount = 0
        )
    }
}