package cinescout.rating.domain.sample

import cinescout.rating.domain.model.MovieWithGenreSlugsAndPersonalRating
import cinescout.rating.domain.model.TvShowWithGenreSlugsAndPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.ScreenplayWithGenreSlugsSample

object ScreenplayWithGenreSlugsAndPersonalRatingSample {

    val BreakingBad = TvShowWithGenreSlugsAndPersonalRating(
        genreSlugs = ScreenplayWithGenreSlugsSample.BreakingBad.genreSlugs,
        personalRating = Rating.of(7).getOrThrow(),
        screenplay = ScreenplaySample.BreakingBad
    )

    val Dexter = TvShowWithGenreSlugsAndPersonalRating(
        genreSlugs = ScreenplayWithGenreSlugsSample.Dexter.genreSlugs,
        personalRating = Rating.of(8).getOrThrow(),
        screenplay = ScreenplaySample.Dexter
    )

    val Grimm = TvShowWithGenreSlugsAndPersonalRating(
        genreSlugs = ScreenplayWithGenreSlugsSample.Grimm.genreSlugs,
        personalRating = Rating.of(9).getOrThrow(),
        screenplay = ScreenplaySample.Grimm
    )

    val Inception = MovieWithGenreSlugsAndPersonalRating(
        genreSlugs = ScreenplayWithGenreSlugsSample.Inception.genreSlugs,
        personalRating = Rating.of(9).getOrThrow(),
        screenplay = ScreenplaySample.Inception
    )

    val TheWolfOfWallStreet = MovieWithGenreSlugsAndPersonalRating(
        genreSlugs = ScreenplayWithGenreSlugsSample.TheWolfOfWallStreet.genreSlugs,
        personalRating = Rating.of(8).getOrThrow(),
        screenplay = ScreenplaySample.TheWolfOfWallStreet
    )

    val War = MovieWithGenreSlugsAndPersonalRating(
        genreSlugs = ScreenplayWithGenreSlugsSample.War.genreSlugs,
        personalRating = Rating.of(6).getOrThrow(),
        screenplay = ScreenplaySample.War
    )
}
