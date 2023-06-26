package cinescout.screenplay.domain.sample

import cinescout.screenplay.domain.model.MovieWithGenreSlugs
import cinescout.screenplay.domain.model.TvShowWithGenreSlugs

object ScreenplayWithGenreSlugsSample {

    val Avatar3 = MovieWithGenreSlugs(
        genreSlugs = listOf(
            GenreSample.Action.slug,
            GenreSample.Adventure.slug,
            GenreSample.ScienceFiction.slug
        ),
        screenplay = ScreenplaySample.Avatar3
    )

    val BreakingBad = TvShowWithGenreSlugs(
        genreSlugs = listOf(
            GenreSample.Drama.slug,
            GenreSample.Crime.slug
        ),
        screenplay = ScreenplaySample.BreakingBad
    )
    val Dexter = TvShowWithGenreSlugs(
        genreSlugs = listOf(
            GenreSample.Drama.slug,
            GenreSample.Mystery.slug,
            GenreSample.Crime.slug
        ),
        screenplay = ScreenplaySample.Dexter
    )
    val Grimm = TvShowWithGenreSlugs(
        genreSlugs = listOf(
            GenreSample.Drama.slug,
            GenreSample.Fantasy.slug,
            GenreSample.Mystery.slug,
            GenreSample.ScienceFiction.slug
        ),
        screenplay = ScreenplaySample.Grimm
    )
    val Inception = MovieWithGenreSlugs(
        genreSlugs = listOf(
            GenreSample.Action.slug,
            GenreSample.Adventure.slug,
            GenreSample.ScienceFiction.slug
        ),
        screenplay = ScreenplaySample.Inception
    )
    val TheWalkingDeadDeadCity = TvShowWithGenreSlugs(
        genreSlugs = listOf(
            GenreSample.Action.slug,
            GenreSample.Adventure.slug,
            GenreSample.Drama.slug,
            GenreSample.Fantasy.slug,
            GenreSample.ScienceFiction.slug
        ),
        screenplay = ScreenplaySample.TheWalkingDeadDeadCity
    )
    val TheWolfOfWallStreet = MovieWithGenreSlugs(
        genreSlugs = listOf(
            GenreSample.Comedy.slug,
            GenreSample.Crime.slug,
            GenreSample.Drama.slug
        ),
        screenplay = ScreenplaySample.TheWolfOfWallStreet
    )
    val War = MovieWithGenreSlugs(
        genreSlugs = listOf(
            GenreSample.Action.slug,
            GenreSample.Adventure.slug,
            GenreSample.Thriller.slug
        ),
        screenplay = ScreenplaySample.War
    )
}
