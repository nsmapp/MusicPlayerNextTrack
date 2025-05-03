package by.niaprauski.player.models


data class PlayerState(
    val isLoading: Boolean,
    val trackCount: Int,
) {
    companion object {

        val INITIAL = PlayerState(
            isLoading = true,
            trackCount = 0
        )
    }
}