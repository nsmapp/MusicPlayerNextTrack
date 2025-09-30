package by.niaprauski.nt.models

data class MainState(
    val isNightMode: Boolean,
){
    companion object{
        val INITIAL = MainState(
            isNightMode = true,
        )
    }
}
