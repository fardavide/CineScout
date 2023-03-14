package cinescout.details.domain.model

import arrow.core.Option
import cinescout.people.domain.model.MovieCredits
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.model.TvShowCredits
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.MovieKeywords
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.TvShowKeywords

sealed interface ScreenplayWithExtras {
    val screenplay: Screenplay
    val credits: ScreenplayCredits
    val genres: ScreenplayGenres
    val isInWatchlist: Boolean
    val keywords: ScreenplayKeywords
    val personalRating: Option<Rating>

    companion object {

        fun from(
            screenplay: Screenplay,
            credits: ScreenplayCredits,
            genres: ScreenplayGenres,
            isInWatchlist: Boolean,
            keywords: ScreenplayKeywords,
            personalRating: Option<Rating>
        ): ScreenplayWithExtras = when (screenplay) {
            is Movie -> MovieWithExtras(
                screenplay = screenplay,
                credits = credits as MovieCredits,
                genres = genres,
                isInWatchlist = isInWatchlist,
                keywords = keywords as MovieKeywords,
                personalRating = personalRating
            )
            is TvShow -> TvShowWithExtras(
                screenplay = screenplay,
                credits = credits as TvShowCredits,
                genres = genres,
                isInWatchlist = isInWatchlist,
                keywords = keywords as TvShowKeywords,
                personalRating = personalRating
            )
        }
    }
}

data class MovieWithExtras(
    override val screenplay: Movie,
    override val credits: MovieCredits,
    override val genres: ScreenplayGenres,
    override val isInWatchlist: Boolean,
    override val keywords: MovieKeywords,
    override val personalRating: Option<Rating>
) : ScreenplayWithExtras

data class TvShowWithExtras(
    override val screenplay: TvShow,
    override val credits: TvShowCredits,
    override val genres: ScreenplayGenres,
    override val isInWatchlist: Boolean,
    override val keywords: TvShowKeywords,
    override val personalRating: Option<Rating>
) : ScreenplayWithExtras
