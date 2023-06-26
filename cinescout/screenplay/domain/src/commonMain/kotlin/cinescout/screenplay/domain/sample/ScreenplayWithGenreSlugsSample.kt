package cinescout.screenplay.domain.sample

import arrow.core.nonEmptyListOf
import cinescout.screenplay.domain.model.MovieWithGenreSlugs
import cinescout.screenplay.domain.model.TvShowWithGenreSlugs

object ScreenplayWithGenreSlugsSample {

    val Avatar3 = MovieWithGenreSlugs(
        genreSlugs = nonEmptyListOf(
            GenreSample.Action.slug,
            GenreSample.Adventure.slug,
            GenreSample.ScienceFiction.slug
        ),
        screenplay = ScreenplaySample.Avatar3
    )

    val BreakingBad = TvShowWithGenreSlugs(
        genreSlugs = nonEmptyListOf(
            GenreSample.Drama.slug,
            GenreSample.Crime.slug
        ),
        screenplay = ScreenplaySample.BreakingBad
    )
    val Dexter = TvShowWithGenreSlugs(
        genreSlugs = nonEmptyListOf(
            GenreSample.Drama.slug,
            GenreSample.Mystery.slug,
            GenreSample.Crime.slug
        ),
        screenplay = ScreenplaySample.Dexter
    )
    val Grimm = TvShowWithGenreSlugs(
        genreSlugs = nonEmptyListOf(
            GenreSample.Drama.slug,
            GenreSample.Fantasy.slug,
            GenreSample.Mystery.slug,
            GenreSample.ScienceFiction.slug
        ),
        screenplay = ScreenplaySample.Grimm
    )
    val Inception = MovieWithGenreSlugs(
        genreSlugs = nonEmptyListOf(
            GenreSample.Action.slug,
            GenreSample.Adventure.slug,
            GenreSample.ScienceFiction.slug
        ),
        screenplay = ScreenplaySample.Inception
    )
    val TheWalkingDeadDeadCity = TvShowWithGenreSlugs(
        genreSlugs = nonEmptyListOf(
            GenreSample.Action.slug,
            GenreSample.Adventure.slug,
            GenreSample.Drama.slug,
            GenreSample.Fantasy.slug,
            GenreSample.ScienceFiction.slug
        ),
        screenplay = ScreenplaySample.TheWalkingDeadDeadCity
    )
    val TheWolfOfWallStreet = MovieWithGenreSlugs(
        genreSlugs = nonEmptyListOf(
            GenreSample.Comedy.slug,
            GenreSample.Crime.slug,
            GenreSample.Drama.slug
        ),
        screenplay = ScreenplaySample.TheWolfOfWallStreet
    )
    val War = MovieWithGenreSlugs(
        genreSlugs = nonEmptyListOf(
            GenreSample.Action.slug,
            GenreSample.Adventure.slug,
            GenreSample.Thriller.slug
        ),
        screenplay = ScreenplaySample.War
    )
}
