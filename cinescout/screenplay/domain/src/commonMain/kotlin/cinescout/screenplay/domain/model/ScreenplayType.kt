package cinescout.screenplay.domain.model

enum class ScreenplayType {
    All, Movies, TvShows;

    fun string() = name.lowercase()
}
