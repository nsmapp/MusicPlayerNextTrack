package by.niaprauski.nt.models

data class MainState(
    val isLoading: Boolean,
    val accentColor: String,
    val backgroundColor: String,
){
    companion object{
        val INITIAL = MainState(
            isLoading = false,
            accentColor = "#E5E5E1",
            backgroundColor = "#FF65A591",
        )
    }
}
