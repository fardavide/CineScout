package cinescout.screenplay.domain.model

import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TvShowIds

sealed interface ScreenplayGenres {

    val genres: List<Genre>
    val screenplayIds: ScreenplayIds
}

fun ScreenplayGenres(genres: List<Genre>, screenplayIds: ScreenplayIds) = when (screenplayIds) {
    is MovieIds -> MovieGenres(genres, screenplayIds)
    is TvShowIds -> TvShowGenres(genres, screenplayIds)
}

data class MovieGenres(
    override val genres: List<Genre>,
    override val screenplayIds: ScreenplayIds
) : ScreenplayGenres

data class TvShowGenres(
    override val genres: List<Genre>,
    override val screenplayIds: ScreenplayIds
) : ScreenplayGenres
