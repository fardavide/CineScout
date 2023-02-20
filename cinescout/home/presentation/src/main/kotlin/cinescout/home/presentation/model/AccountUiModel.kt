package cinescout.home.presentation.model

data class AccountUiModel(
    val imageUrl: String?,
    val source: Source,
    val username: String
) {

    enum class Source {
        Tmdb,
        Trakt
    }
}
