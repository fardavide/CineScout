package cinescout.screenplay.domain.model

import arrow.core.Nel
import arrow.optics.optics
import cinescout.screenplay.domain.model.id.GenreSlug

@optics sealed interface ScreenplayWithGenreSlugs {
    val screenplay: Screenplay
    val genreSlugs: Nel<GenreSlug>

    companion object
}

fun ScreenplayWithGenreSlugs(screenplay: Screenplay, genreSlugs: Nel<GenreSlug>): ScreenplayWithGenreSlugs =
    when (screenplay) {
        is Movie -> MovieWithGenreSlugs(genreSlugs, screenplay)
        is TvShow -> TvShowWithGenreSlugs(genreSlugs, screenplay)
    }

@optics data class MovieWithGenreSlugs(
    override val genreSlugs: Nel<GenreSlug>,
    override val screenplay: Movie
) : ScreenplayWithGenreSlugs {

    companion object
}

@optics data class TvShowWithGenreSlugs(
    override val genreSlugs: Nel<GenreSlug>,
    override val screenplay: TvShow
) : ScreenplayWithGenreSlugs {

    companion object
}
