package cinescout.suggestions.domain.sample

import cinescout.details.domain.sample.ScreenplayWithExtrasSample
import cinescout.screenplay.domain.model.MovieGenres
import cinescout.screenplay.domain.model.TvShowGenres
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.suggestions.domain.model.SuggestedMovieWithExtras
import cinescout.suggestions.domain.model.SuggestedTvShowWithExtras

object SuggestedScreenplayWithExtrasSample {

    val BreakingBad = SuggestedTvShowWithExtras(
        affinity = SuggestedScreenplaySample.BreakingBad.affinity,
        credits = ScreenplayWithExtrasSample.BreakingBad.credits,
        genres = ScreenplayWithExtrasSample.BreakingBad.genres as TvShowGenres,
        isInWatchlist = ScreenplayWithExtrasSample.BreakingBad.isInWatchlist,
        keywords = ScreenplayWithExtrasSample.BreakingBad.keywords,
        personalRating = ScreenplayWithExtrasSample.BreakingBad.personalRating,
        screenplay = ScreenplaySample.BreakingBad,
        source = SuggestedScreenplaySample.BreakingBad.source
    )

    val Dexter = SuggestedTvShowWithExtras(
        affinity = SuggestedScreenplaySample.Dexter.affinity,
        credits = ScreenplayWithExtrasSample.Dexter.credits,
        genres = ScreenplayWithExtrasSample.Dexter.genres as TvShowGenres,
        isInWatchlist = ScreenplayWithExtrasSample.Dexter.isInWatchlist,
        keywords = ScreenplayWithExtrasSample.Dexter.keywords,
        personalRating = ScreenplayWithExtrasSample.Dexter.personalRating,
        screenplay = ScreenplaySample.Dexter,
        source = SuggestedScreenplaySample.Dexter.source
    )

    val Grimm = SuggestedTvShowWithExtras(
        affinity = SuggestedScreenplaySample.Grimm.affinity,
        credits = ScreenplayWithExtrasSample.Grimm.credits,
        genres = ScreenplayWithExtrasSample.Grimm.genres as TvShowGenres,
        isInWatchlist = ScreenplayWithExtrasSample.Grimm.isInWatchlist,
        keywords = ScreenplayWithExtrasSample.Grimm.keywords,
        personalRating = ScreenplayWithExtrasSample.Grimm.personalRating,
        screenplay = ScreenplaySample.Grimm,
        source = SuggestedScreenplaySample.Grimm.source
    )

    val Inception = SuggestedMovieWithExtras(
        affinity = SuggestedScreenplaySample.Inception.affinity,
        credits = ScreenplayWithExtrasSample.Inception.credits,
        genres = ScreenplayWithExtrasSample.Inception.genres as MovieGenres,
        isInWatchlist = ScreenplayWithExtrasSample.Inception.isInWatchlist,
        keywords = ScreenplayWithExtrasSample.Inception.keywords,
        personalRating = ScreenplayWithExtrasSample.Inception.personalRating,
        screenplay = ScreenplaySample.Inception,
        source = SuggestedScreenplaySample.Inception.source
    )

    val TheWolfOfWallStreet = SuggestedMovieWithExtras(
        affinity = SuggestedScreenplaySample.TheWolfOfWallStreet.affinity,
        credits = ScreenplayWithExtrasSample.TheWolfOfWallStreet.credits,
        genres = ScreenplayWithExtrasSample.TheWolfOfWallStreet.genres as MovieGenres,
        isInWatchlist = ScreenplayWithExtrasSample.TheWolfOfWallStreet.isInWatchlist,
        keywords = ScreenplayWithExtrasSample.TheWolfOfWallStreet.keywords,
        personalRating = ScreenplayWithExtrasSample.TheWolfOfWallStreet.personalRating,
        screenplay = ScreenplaySample.TheWolfOfWallStreet,
        source = SuggestedScreenplaySample.TheWolfOfWallStreet.source
    )

    val War = SuggestedMovieWithExtras(
        affinity = SuggestedScreenplaySample.War.affinity,
        credits = ScreenplayWithExtrasSample.War.credits,
        genres = ScreenplayWithExtrasSample.War.genres as MovieGenres,
        isInWatchlist = ScreenplayWithExtrasSample.War.isInWatchlist,
        keywords = ScreenplayWithExtrasSample.War.keywords,
        personalRating = ScreenplayWithExtrasSample.War.personalRating,
        screenplay = ScreenplaySample.War,
        source = SuggestedScreenplaySample.War.source
    )
}
