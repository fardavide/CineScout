package cinescout.screenplay.domain.model

import arrow.optics.optics
import cinescout.screenplay.domain.model.id.GenreSlug
import cinescout.screenplay.domain.model.id.ScreenplayIds

@optics
sealed interface ScreenplayWithGenreSlugs {

    val screenplay: Screenplay
    val genreSlugs: List<GenreSlug>

    companion object
}

fun ScreenplayWithGenreSlugs(screenplay: Screenplay, genreSlugs: List<GenreSlug>): ScreenplayWithGenreSlugs =
    when (screenplay) {
        is Movie -> MovieWithGenreSlugs(genreSlugs, screenplay)
        is TvShow -> TvShowWithGenreSlugs(genreSlugs, screenplay)
    }

@optics
data class MovieWithGenreSlugs(
    override val genreSlugs: List<GenreSlug>,
    override val screenplay: Movie
) : ScreenplayWithGenreSlugs {

    companion object
}

@optics
data class TvShowWithGenreSlugs(
    override val genreSlugs: List<GenreSlug>,
    override val screenplay: TvShow
) : ScreenplayWithGenreSlugs {

    companion object
}

fun List<ScreenplayWithGenreSlugs>.ids(): List<ScreenplayIds> = map { it.screenplay.ids }

fun List<ScreenplayWithGenreSlugs>.filterByType(type: ScreenplayTypeFilter): List<ScreenplayWithGenreSlugs> =
    when (type) {
        ScreenplayTypeFilter.All -> this
        ScreenplayTypeFilter.Movies -> filterIsInstance<MovieWithGenreSlugs>()
        ScreenplayTypeFilter.TvShows -> filterIsInstance<TvShowWithGenreSlugs>()
    }
