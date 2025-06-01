package by.niaprauski.domain.models

data class SearchTrackFilter(
    val text: String,
){
    companion object{
        val DEFAULT = SearchTrackFilter(
            text = ""
        )
    }
}