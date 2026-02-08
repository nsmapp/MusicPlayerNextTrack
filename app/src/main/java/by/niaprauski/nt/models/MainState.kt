package by.niaprauski.nt.models

data class MainState(
    val isLoading: Boolean,
    val isNightMode: Boolean,
){
    companion object{
        val INITIAL = MainState(
            isLoading = false,
            isNightMode = true,
        )
    }
}
