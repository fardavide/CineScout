package cinescout.screenplay.domain.model

enum class ScreenplayType {

    Movie, TvShow;

    companion object {
        fun from(ids: ScreenplayIds) = when (ids) {
            is ScreenplayIds.Movie -> Movie
            is ScreenplayIds.TvShow -> TvShow
        }
    }
}
